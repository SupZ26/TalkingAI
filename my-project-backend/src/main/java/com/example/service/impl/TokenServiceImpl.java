package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.Account;
import com.example.entity.dto.TokenBind;
import com.example.mapper.AccountMapper;
import com.example.mapper.TokenBindMapper;
import com.example.service.TokenService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    AccountMapper accountMapper;
    @Resource
    TokenBindMapper tokenBindMapper;

    @Value("${OpenKey.web_server}")
    String url;

    /**
     * 从官网查询token的信息
     */
    public Map<String, Object> getToken(String username) {
        String apiKey = getTokenIdByUsername(username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("api_key", apiKey);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get token: " + response.getStatusCode());
        }
    }

    /**
     * 为新用户绑定tokenId
     * @param username
     * @return
     */
    @Override
    public void bindToken(String username) {
        QueryWrapper<TokenBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("username").last("limit 1"); // 查询username为空的记录
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


}
