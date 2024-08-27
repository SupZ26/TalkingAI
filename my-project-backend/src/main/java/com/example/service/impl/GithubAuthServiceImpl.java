package com.example.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.GithubAuthVO;
import com.example.mapper.AccountMapper;
import com.example.service.GithubAuthService;
import com.example.utils.Const;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class GithubAuthServiceImpl implements GithubAuthService {
    @Resource
    AccountMapper accountMapper;
    @Resource
    PasswordEncoder passwordEncoder;


    /**
     * 查询是否有人绑定了指定的githubId，如果没有自动创建用户
     * @param githubName
     * @param githubId
     * @return
     */
    @Override
    public void isBondWithGithub(String githubName, Integer githubId,String githubEmail ) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("githubId",githubId);
        Account account = accountMapper.selectOne(queryWrapper);

        Optional<Integer> optionalGithubId = Optional.ofNullable(account)
                .map(Account::getGithubId);
        if(optionalGithubId.isPresent()){
            log.info("用户已经存在，直接登录");
        }else {
            //创建一个新的角色(默认密码为123456)
            accountMapper.insert(new Account(null,githubName, passwordEncoder.encode(Const.USER_DEFAULT_PASSWORD), githubEmail, Const.ROLE_DEFAULT,new Date(),0.0,0.0,null,githubId));
            log.info("用户不存在，已为其创建新用户: "+githubName);
        }
    }

    /**
     * github登录
     * @param githubId
     * @param githubUsername
     * @param accessToken github的授权码
     */
    @Override
    public void githubLogin(Integer githubId,String githubUsername, String accessToken) {
        String finalResult02 = HttpRequest.get("https://api.github.com/user/emails")
                .header("Authorization","token "+accessToken)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897)))
                .execute().body();
        log.info("finalResult02:"+finalResult02);
        String githubEmail = JsonPath.read(finalResult02,"$[0].email");
        //查看是否有绑定该id的用户
        isBondWithGithub(githubUsername,githubId,githubEmail);
    }


}
