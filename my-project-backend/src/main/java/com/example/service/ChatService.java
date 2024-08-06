package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Chat;
import com.example.entity.vo.request.AIRequestChatVO;
import com.example.entity.vo.request.RequestChatVO;
import com.example.entity.vo.response.AIResponseChatVO;
import org.springframework.stereotype.Service;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Service
public interface ChatService extends IService<Chat> {
    //用户AI聊天
    AIResponseChatVO toChat(RequestChatVO requestChatVO);

    //查询用户在相关主题下的对话记录
    List<Chat> getChatContents(String username, String topic);

    //创建用户以及关联的topic
    void createUserAndTopic(String username, String topic);

    //对接api进行聊天
    AIResponseChatVO aIChat(AIRequestChatVO aiRequestChatVO,String token);



}
