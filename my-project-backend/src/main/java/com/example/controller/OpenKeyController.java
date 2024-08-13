package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.TokenService;
import com.example.service.impl.TokenServiceImpl;
import jakarta.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class OpenKeyController {


    @Autowired
    private TokenService tokenService;

    @PostMapping("/getTokenDetails")
    public Map<String, Object> getTokenDetails(@RequestParam String username) {
        return tokenService.getToken(username);
    }

    @GetMapping("/bindToken")
    public RestBean<Void> bindToken(@RequestParam String username){
        tokenService.bindToken(username);
        return RestBean.success();
    }

}
