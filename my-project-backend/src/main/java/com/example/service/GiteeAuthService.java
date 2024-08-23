package com.example.service;

import org.springframework.stereotype.Service;

@Service
public interface GiteeAuthService {
    //gitee登录回调处理
    void callback(String code);

    //gitee获取授权用户的资料
    String getUser(String access_token);

    //判断是否绑定了账号
    Boolean isBondWithGitee(String username);
}
