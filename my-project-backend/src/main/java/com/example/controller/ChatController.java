package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RequestChatVO;
import com.example.entity.vo.response.AIResponseChatVO;
import com.example.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatAI")
public class ChatController {

    @Resource
    ChatService chatService;

    /**
     * 对接api进行ai聊天
     * @param requestChatVO
     * @return
     */
    @PostMapping("/toChat")
    public RestBean<AIResponseChatVO> toChat(@RequestBody RequestChatVO requestChatVO){
        return RestBean.success(chatService.toChat(requestChatVO));
    }

}
