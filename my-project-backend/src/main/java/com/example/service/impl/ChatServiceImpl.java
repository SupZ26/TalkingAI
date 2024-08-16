package com.example.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Chat;
import com.example.entity.vo.request.AIRequestChatVO;
import com.example.entity.vo.request.RequestChatVO;
import com.example.entity.vo.request.UpdateTopicVO;
import com.example.entity.vo.response.AIResponseChatVO;
import com.example.mapper.ChatMapper;
import com.example.service.ChatService;
import com.example.service.TokenService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Resource
    ChatMapper chatMapper;

    @Resource
    RestTemplate restTemplate;

    @Value("${OpenKey.gpt_server}")
    String gpt_url;
    @Resource
    TokenService tokenService;

    /**
     * 对接api聊天，gpt采用流式输出，后端使用sse推流到前端
     */
    private static final Pattern contentPattern = Pattern.compile("\"content\":\"(.*?)\"}");
    @Async
    public void question(RequestChatVO requestChatVO, String question, SseEmitter sseEmitter) {
        String API_KEY = tokenService.getTokenIdByUsername(requestChatVO.getUsername());
        StringBuilder buffer = new StringBuilder();
        StringBuilder total_res = new StringBuilder();
        try {
            // 构建请求参数
            String params = "{\"model\":\"" + requestChatVO.getModel() + "\",\"messages\":[{\"role\":\"user\",\"content\":\"" + question + "\"}],\"stream\":true}";

            // 发起 HTTP 请求
            HttpRequest httpRequest = HttpRequest.post(gpt_url)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .body(params)
                    // clash代理
                    .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897)));

            // 执行 HTTP 请求
            HttpResponse execute = httpRequest.execute();

            // 处理响应流
            try (InputStream inputStream = execute.bodyStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.hasLength(line)) {
                        buffer.append(line);

                        // 处理响应内容
                        Matcher matcher = contentPattern.matcher(buffer.toString());
                        if (matcher.find()) {
                            String content = matcher.group(1);
                            total_res.append(content);

                            // 逐字发送内容到前端
                            for (char c : content.toCharArray()) {
                                sseEmitter.send(SseEmitter.event().name("answer").data(String.valueOf(c)));
                                Thread.sleep(100);
                                log.info("content:"+c);
                            }
                        }
                        buffer.setLength(0);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            sseEmitter.complete();
            // 保存对话内容
            addCurrentContents(requestChatVO.getUsername(), requestChatVO.getTopic(), now_content, String.valueOf(total_res));
        }
    }



    /**
     * 用户ai聊天总流程
     * @param requestChatVO 用户端相关数据
     * @return
     */
    public String now_content;
    @Override
    public void toChat(RequestChatVO requestChatVO) {
        String username = requestChatVO.getUsername();
        String topic = requestChatVO.getTopic();
        String token = tokenService.getTokenIdByUsername(username);
        if(getChatContents(username, topic).isEmpty()){
            //之前没有对话的情况，不需要考虑联系上下文
            now_content = requestChatVO.getMessages().get(0).getContent();
            //向ai发出聊天，等待回应,使用post方式
            /*AIRequestChatVO aiRequestChatVO = new AIRequestChatVO();
            BeanUtils.copyProperties(requestChatVO,aiRequestChatVO);
            return aIChat(aiRequestChatVO,token,username,topic);*/
            question(requestChatVO,now_content,new SseEmitter());

        }else {
            //之前有过对话，需要考虑上下文
            //从数据库中获取之前的对话(只需获取问题)
            List<Chat> chatContents = getChatContents(username, topic);
            StringBuilder questions = new StringBuilder(" ");

            for(int i = 0;i < chatContents.size();i++){
                questions.append(chatContents.get(i).getQuestion());
            }
            //将老对话与新的对话进行拼接,变成当前问题
            now_content = requestChatVO.getMessages().get(0).getContent();
            StringBuilder now_contents = new StringBuilder(now_content);
            StringBuilder later_content = questions.append(now_contents);
            String content = String.valueOf(later_content);
            requestChatVO.getMessages().get(0).setContent(content);
            //发送信息进行ai聊天
            /*AIRequestChatVO aiRequestChatVO = new AIRequestChatVO();
            BeanUtils.copyProperties(requestChatVO,aiRequestChatVO);
            return aIChat(aiRequestChatVO,token,username,topic);*/
            question(requestChatVO,content,new SseEmitter());
        }

    }

    /**
     * 对接api进行聊天（老版本聊天，未开启stream流式回复）
     * @param aiRequestChatVO   封装的用户消息和一些必带的字段
     * @return
     */
    @Override
    public AIResponseChatVO aIChat(AIRequestChatVO aiRequestChatVO,String token,String username,String topic) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<AIRequestChatVO> entity = new HttpEntity<>(aiRequestChatVO, headers);
        ResponseEntity<String> response = restTemplate.exchange(gpt_url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            //把新增的对话信息加入到数据库中
            //发出的问题
            String question = aiRequestChatVO.getMessages().get(0).getContent();
            //回答的问题
            AIResponseChatVO vo = JSON.parseObject(response.getBody(), AIResponseChatVO.class);
            String res = vo.getChoices().get(0).getMessage().getContent();
            addCurrentContents(username,topic,now_content,res);
            return vo;
        } else {
            throw new RuntimeException("Failed to get token: " + response.getStatusCode());
        }
    }

    /**
     * 将本次问答加入到数据库中
     *
     * @param question 问题
     * @param response 回答
     */
    @Override
    public void addCurrentContents(String username, String topic,
                                   String question, String response) {
        chatMapper.insert(new Chat(username, topic, question, response));
    }

    /**
     * 查询用户在相关主题下的对话记录
     * @param username 用户名
     * @param topic 对话主题
     * @return
     */
    @Override
    public List<Chat> getChatContents(String username, String topic) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username)
                .eq("topic",topic)
                .orderBy(true,true,"dialog_id");
        return chatMapper.selectList(queryWrapper);
    }

    /**
     * 修改话题
     */
    @Override
    @Transactional
    public int updateTopic(UpdateTopicVO updateTopicVO) {
        System.out.println("===========");
        System.out.println(updateTopicVO.getUsername());
        System.out.println(updateTopicVO.getOldTopic());
        System.out.println(updateTopicVO.getNewTopic());
        return chatMapper.updateTopic(updateTopicVO.getUsername(), updateTopicVO.getOldTopic(), updateTopicVO.getNewTopic());
    }

    /**
     * 查询用户所有拥有的对话主题
     * @param username
     * @return
     */
    @Override
    public List<String> findAllTopic(String username) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return chatMapper.selectList(queryWrapper).stream()
                .map(Chat::getTopic)
                .distinct()
                .toList();

    }

    /**
     * 删除指定的topic
     * @param username
     * @param topic
     * @return
     */
    @Override
    public int deleteTopic(String username, String topic) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username)
                .eq("topic",topic);
        return chatMapper.delete(queryWrapper);
    }


}
