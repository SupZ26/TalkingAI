package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 官方返回的对话数据进行封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIResponseChatVO {
    String id;
    String object;
    int created;
    String model;
    List<Choice> choices;
    Usage usage;
    String system_fingerprint;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {
        int index;
        Message message;
        Object logprobs; // 因为 logprobs 是 null，定义为 Object 可以兼容 null
        String finish_reason;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        String role;
        String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage {
        int prompt_tokens;
        int completion_tokens;
        int total_tokens;
    }
}
