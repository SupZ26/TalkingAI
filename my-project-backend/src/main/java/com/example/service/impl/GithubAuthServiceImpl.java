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

    /**
     * github登录回调处理
     * @param code
     * @return
     */
    @Override
    public String githubCallback(String code) {
        Map<String, Object > paramMap = new HashMap<>();
        paramMap.put("client_id","Ov23liUpnU7nof5IQO9G");
        paramMap.put("client_secret","5a720f33149bb7dc1ce0fcc892bc84c7a5f7523f");
        paramMap.put("code",code);
        paramMap.put("accept","json");
        String result = HttpUtil.post("https://github.com/login/oauth/access_token",paramMap);
        String token = result.split("&")[0].split("=")[1];
        // 获取用户信息，发起get 请求，拿到用户信息
        String finalResult = HttpRequest.get("https://api.github.com/user")
                .header("Authorization","token "+token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7897)))
                .execute().body();
        String githubUsername = JsonPath.read(finalResult, "$.login");
        Integer githubId = JsonPath.read(finalResult,"$.id");

        //查看是否有绑定该id的用户
        return isBondWithGithub(githubUsername,githubId);
    }

    /**
     * 查询是否有人绑定了指定的githubId，如果没有自动创建用户
     * @param githubName
     * @param githubId
     * @return
     */
    @Override
    public String isBondWithGithub(String githubName, Integer githubId) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("githubId",githubId);
        Account account = accountMapper.selectOne(queryWrapper);
        Optional<Integer> optionalGithubId = Optional.ofNullable(account)
                .map(Account::getGithubId);
        if(optionalGithubId.isPresent()){
            log.info("用户已经存在，直接登录");
            return account.getUsername();
        }else {
            //创建一个新的角色(默认密码为123456)
            accountMapper.insert(new Account(null,githubName,"123456",null, Const.ROLE_DEFAULT,new Date(),0.0,0.0,null,githubId));
            log.info("用户不存在，已为其创建新用户: "+githubName);
            return githubName;
        }
    }


}
