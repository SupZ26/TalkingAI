package com.example.service;

import com.example.entity.vo.response.GithubAuthVO;
import org.springframework.stereotype.Service;

@Service
public interface GithubAuthService {
    //处理github第三方登录回调
    String githubCallback(String code);

    //查询是否有人绑定了指定的githubId，如果没有自动创建用户
    String isBondWithGithub(String githubName,Integer githubId,String githubEmail);
}
