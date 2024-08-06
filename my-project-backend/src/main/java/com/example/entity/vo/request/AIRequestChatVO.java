package com.example.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 后端向接口发送的数据封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequestChatVO {

    //语言模型
    String model;
    //消息
    List<RequestChatVO.Message> messages;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message{
        String role;
        String content;
    }
}
