package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Chat;
import com.example.entity.vo.request.RequestChatVO;
import com.example.entity.vo.request.UpdateTopicVO;
import com.example.entity.vo.response.AIResponseChatVO;
import com.example.service.ChatService;
import com.example.utils.Const;
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
     * @return
     */
    @GetMapping("/findAllTopic")
    public RestBean<List<String>> findAllTopic(@RequestAttribute(Const.ATTR_USER_ID)int id){
        if(chatService.findAllTopic(id).isEmpty())
            return RestBean.failure(500,"找不到主题");
        return RestBean.success(chatService.findAllTopic(id));
    }

    /**
     * 删除指定对话主题
     * @param id
     * @param topic
     * @return
     */
    @DeleteMapping("/deleteTopic")
    public RestBean<Void> deleteTopic(@RequestAttribute(Const.ATTR_USER_ID)int id,@RequestParam String topic){
        if(chatService.deleteTopic(id, topic) != 0){
            return RestBean.success();
        }else {
            return RestBean.failure(500,"删除错误");
        }
    }

    @GetMapping("/getChatContents")
    public RestBean<List<Chat>> getChatContents(@RequestParam("username") String username, @RequestParam("topic") String topic){
        return RestBean.success(chatService.getChatContents(username,topic));
    }




}
