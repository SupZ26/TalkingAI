package com.example.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Chat;
import com.example.entity.vo.request.AIRequestChatVO;
import com.example.entity.vo.request.RequestChatVO;
import com.example.entity.vo.response.AIResponseChatVO;
import com.example.mapper.ChatMapper;
import com.example.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.ByteBuffer;
import java.util.*;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Resource
    ChatMapper chatMapper;

    @Resource
    RestTemplate restTemplate;

    @Value("${OpenKey.gpt_server}")
    String gpt_url;

    /**
     * 用户ai聊天总流程
     * @param requestChatVO 用户端相关数据
     * @return
     */
    @Override
    public AIResponseChatVO toChat(RequestChatVO requestChatVO) {
        String username = requestChatVO.getUsername();
        String topic = requestChatVO.getTopic();
        String token = requestChatVO.getToken();
        if(getChatContents(username,topic).get(0).getQuestion() == null){
            //之前没有对话的情况，不需要考虑联系上下文
            //创建主题对话
            createUserAndTopic(username,topic);
            //向ai发出聊天，等待回应,使用post方式
            AIRequestChatVO aiRequestChatVO = new AIRequestChatVO();
            BeanUtils.copyProperties(requestChatVO,aiRequestChatVO);
            return aIChat(aiRequestChatVO,token);

        }else {
            //之前有过对话，需要考虑上下文
            //从数据库中获取之前的对话(只需获取问题)
            List<Chat> chatContents = getChatContents(username, topic);
            StringBuilder questions = new StringBuilder(" ");

            for(int i = 0;i < chatContents.size();i++){
                questions.append(chatContents.get(i).getQuestion());
            }
            //将老对话与新的对话进行拼接,变成当前问题
            String now_content = requestChatVO.getMessages().get(0).getContent();
            StringBuilder now_contents = new StringBuilder(now_content);
            StringBuilder later_content = questions.append(now_contents);
            String content = String.valueOf(later_content);
            requestChatVO.getMessages().get(0).setContent(content);
            //发送信息进行ai聊天
            AIRequestChatVO aiRequestChatVO = new AIRequestChatVO();
            BeanUtils.copyProperties(requestChatVO,aiRequestChatVO);
            return aIChat(aiRequestChatVO,token);

        }

    }

    /**
     * 对接api进行聊天
     * @param aiRequestChatVO   封装的用户消息和一些必带的字段
     * @return
     */
    @Override
    public AIResponseChatVO aIChat(AIRequestChatVO aiRequestChatVO,String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<AIRequestChatVO> entity = new HttpEntity<>(aiRequestChatVO, headers);
        ResponseEntity<String> response = restTemplate.exchange(gpt_url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            //TODO 把新增的对话信息加入到数据库中
            return JSON.parseObject(response.getBody(), AIResponseChatVO.class);
        } else {
            throw new RuntimeException("Failed to get token: " + response.getStatusCode());
        }
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
                .eq("topic",topic);
        return chatMapper.selectList(queryWrapper);
    }

    /**
     * 创建用户以及关联的topic
     *
     * @param username 用户名
     * @param topic    对话主题
     */
    @Override
    public void createUserAndTopic(String username, String topic) {
        UpdateWrapper<Chat> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",username)
                        .set("topic",topic);
        int update = chatMapper.update(null, updateWrapper);
        if(update != 0){
        }else {
            throw new RuntimeException("创建失败，请重试");
        }
    }




}
