package com.example.service;

import com.example.entity.dto.Account;
import com.example.entity.vo.response.GithubAuthVO;
import org.springframework.stereotype.Service;

@Service
public interface GithubAuthService {


    //查询是否有人绑定了指定的githubId，如果没有自动创建用户
    void isBondWithGithub(String githubName, Integer githubId, String githubEmail);

    //github登录
    void githubLogin(Integer githubId,String githubUsername,String accessToken);
}
