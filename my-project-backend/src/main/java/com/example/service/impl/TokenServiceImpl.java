package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.dto.Account;
import com.example.mapper.AccountMapper;
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

    @Value("${OpenKey.web_server}")
    String url;

    /**
     * 从官网查询token的信息
     * @param apiKey 官网令牌号
     */
    public Map<String, Object> getToken(String apiKey) {

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

}
