package com.example.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.Account;
import com.example.mapper.AccountMapper;
import com.example.service.GiteeAuthService;
import com.example.utils.Const;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GiteeAuthServiceImpl implements GiteeAuthService {
    @Resource
    AccountMapper accountMapper;
    @Resource
    PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.gitee.client-id}")
    String client_id;
    @Value("${spring.security.oauth2.client.registration.gitee.redirect-uri}")
    String redirect_uri;
    @Value("${spring.security.oauth2.client.registration.gitee.client-secret}")
    String client_secret;

    /**
     * 处理gitee登录回调，发起请求获取access_token
     * @param code
     */
    @Override
    public void callback(String code) {
        Map<String,Object> param = new HashMap<>();
        param.put("grant_type","authorization_code");
        param.put("code",code);
        param.put("client_id",client_id);
        param.put("redirect_uri",redirect_uri);
        param.put("client_secret",client_secret);
        String json = JSONUtil.toJsonStr(param);
        String result = HttpRequest.post("https://gitee.com/oauth/token")
                .body(json)
                .execute().body();
        String access_token = JsonPath.read(result,"$.access_token");
        getUser(access_token);


    }

    /**
     * gitee获取授权用户的资料
     * @param access_token
     * @return
     */
    @Override
    public String getUser(String access_token) {
        Map<String,Object> param = new HashMap<>();
        param.put("access_token",access_token);
        String result = HttpUtil.get("https://gitee.com/api/v5/user",param);
        String name = JsonPath.read(result,"$.name");
        Integer giteeId = JsonPath.read(result,"$.id");

        //判断是否绑定了账号，没有就创建账号
        if(isBondWithGitee(name)){
            log.info("已绑定账号，直接登录！");
            return name;
        }else {
            accountMapper.insert(new Account(null,name, passwordEncoder.encode(Const.USER_DEFAULT_PASSWORD),null,"user",new Date(),0,0,null,giteeId));
            log.info("未绑定账号，将自动创建账号");
            return name;
        }
    }

    /**
     * 查看账号是否绑定了gitee
     * @param username
     * @return
     */
    @Override
    public Boolean isBondWithGitee(String username) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return accountMapper.selectOne(queryWrapper) != null;
    }
}
