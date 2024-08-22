package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RequestChatVO;
import com.example.entity.vo.request.UpdateTopicVO;
import com.example.entity.vo.response.AIResponseChatVO;
import com.example.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

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
    public void toChat(@RequestBody RequestChatVO requestChatVO){
        chatService.toChat(requestChatVO);
    }

    /**
     * 更改主题
     * @param updateTopicVO
     * @return
     */
    @PostMapping("/updateTopic")
    public RestBean<Integer> updateTopic(@RequestBody UpdateTopicVO updateTopicVO){
        return RestBean.success(chatService.updateTopic(updateTopicVO));
    }

    /**
     * 查找所有的主题
     * @param username
     * @return
     */
    @GetMapping("/findAllTopic")
    public RestBean<List<String>> findAllTopic(@RequestParam String username){
        if(chatService.findAllTopic(username).isEmpty())
            return RestBean.failure(500,"找不到主题");
        return RestBean.success(chatService.findAllTopic(username));
    }

    @DeleteMapping("/deleteTopic")
    public RestBean<Void> deleteTopic(@RequestParam String username,@RequestParam String topic){
        if(chatService.deleteTopic(username, topic) != 0){
            return RestBean.success();
        }else {
            return RestBean.failure(500,"删除错误");
        }
    }




}
