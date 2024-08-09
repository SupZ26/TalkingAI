package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public interface WeiXinAuthService {

    //获取access_token
    void callBack(String code);
    //获取微信任务信息
    String getUserInfo(String accessToken);
}
