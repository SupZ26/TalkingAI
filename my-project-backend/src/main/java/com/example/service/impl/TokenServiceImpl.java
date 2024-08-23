package com.example.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.entity.dto.Account;
import com.example.entity.dto.TokenBind;
import com.example.mapper.AccountMapper;
import com.example.mapper.TokenBindMapper;
import com.example.service.AccountDetailsService;
import com.example.service.TokenService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    AccountMapper accountMapper;
    @Resource
    TokenBindMapper tokenBindMapper;
    @Resource
    AccountDetailsService accountDetailsService;

    @Value("${OpenKey.web_server}")
    String get_token_url;

    /**
     * 从官网查询token的信息
     */
    public String getToken(int id) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        String username = accountMapper.selectOne(queryWrapper).getUsername();
        Map<String,Object> paramMap = new HashMap<>();
        String api_key = getTokenIdByUsername(username);
        paramMap.put("api_key",api_key);
        String json = JSONUtil.toJsonStr(paramMap);
        String result = HttpRequest.post(get_token_url)
                        .body(json).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(result);
        log.info(jsonObject.getStr("Remaining"));
        return jsonObject.getStr("Remaining");
    }

    /**
     * 为新用户绑定tokenId
     * @return
     */
    @Override
    public void bindToken(int id) {
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("id",id);
        String username = accountMapper.selectOne(accountQueryWrapper).getUsername();

        QueryWrapper<TokenBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username","").last("limit 1");
        TokenBind tokenEntity = tokenBindMapper.selectOne(queryWrapper);

        if (tokenEntity != null) {
            tokenEntity.setUsername(username);
            tokenBindMapper.updateById(tokenEntity);
        } else {
            throw new RuntimeException("No available tokenId found.");
        }

    }

    /**
     * 查询用户绑定的tokenId
     * @param username
     * @return
     */
    @Override
    public String getTokenIdByUsername(String username) {
        QueryWrapper<TokenBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return tokenBindMapper.selectOne(queryWrapper).getTokenId();
    }

    /**
     * 使用余额买key
     */
    @Override
    @Transactional
    public void buyKeyByDeposit(int id)  {
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("id",id);
        String username = accountMapper.selectOne(accountQueryWrapper).getUsername();

        if(isBindWithToken(username))
            throw new RuntimeException("该用户已经绑定token，无法再次购买");
        UpdateWrapper<Account> updateWrapper  = new UpdateWrapper<>();
        updateWrapper.eq("username",username);
        try{
            updateWrapper.setSql("deposit = deposit - 20 ");
            accountMapper.update(null,updateWrapper);
        }catch (Exception e){
           log.info(e.getMessage());
        }
        bindToken(id);
        accountDetailsService.updateToken(username,Double.parseDouble(getToken(id)));
    }

    /**
     * 支付宝支付购买key
     * @param username
     */
    @Override
    @Transactional
    public void buyKeyByAlipay(String username) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Integer id = accountMapper.selectOne(queryWrapper).getId();

        if(isBindWithToken(username))
            throw new RuntimeException("该用户已经绑定token，无法再次购买");
        bindToken(id);
        accountDetailsService.updateToken(username,Double.parseDouble(getToken(id)));
    }

    /**
     * 检查用户是否已经绑定了Token
     * @param username
     * @return
     */
    @Override
    public boolean isBindWithToken(String username) {
        QueryWrapper<TokenBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return tokenBindMapper.selectOne(queryWrapper) != null;
    }


}
