package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Chat;
import com.example.entity.vo.request.AIRequestChatVO;
import com.example.entity.vo.request.RequestChatVO;
import com.example.entity.vo.request.UpdateTopicVO;
import com.example.entity.vo.response.AIResponseChatVO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
public interface ChatService extends IService<Chat> {
    //用户AI聊天
    void toChat(RequestChatVO requestChatVO);

    //查询用户在相关主题下的对话记录
    List<Chat> getChatContents(String username, String topic);



    //对接api进行聊天
    AIResponseChatVO aIChat(AIRequestChatVO aiRequestChatVO,String token,String username,String topic);

    //将本次问答加入到数据库中
    void addCurrentContents(String username, String topic, String question, String response);

    //修改话题
    int updateTopic(UpdateTopicVO updateTopicVO);

    //查询用户所有拥有的对话主题
    List<String> findAllTopic(int id);

    //删除指定的对话主题
    int deleteTopic(int id,String topic);

    public void question(RequestChatVO requestChatVO,String question,SseEmitter sseEmitter);
}
