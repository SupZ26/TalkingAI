package com.example.controller;

import com.example.service.impl.TokenServiceImpl;
import jakarta.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class OpenKeyController {


    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    @PostMapping("/getTokenDetails")
    public Map<String, Object> getTokenDetails(@RequestBody Map<String, String> request) {
        String apiKey = request.get("api_key");
        return tokenServiceImpl.getToken(apiKey);
    }

}
