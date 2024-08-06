package com.example;

import com.example.entity.dto.Chat;
import com.example.mapper.ChatMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyProjectBackendApplicationTests {


    @Resource
    ChatMapper chatMapper;
    @Test
    void contextLoads() {
        Chat chat = new Chat("username","topic","question","response");
        chatMapper.insert(chat);
    }
}
