package com.example.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户向服务端发送的对话请求数据封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestChatVO {
    //用户名
    String username;
    //主题
    String topic;
    //令牌
    String token;
    //语言模型
    String model;
    //消息
    List<Message> messages;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message{
        String role;
        String content;
    }

}
