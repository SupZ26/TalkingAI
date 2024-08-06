package com.example.service;


import java.util.Map;

public interface TokenService {
    /**
     * 从官网查询Token的具体信息
     * @param apiKey 官网令牌号
     * @return
     */
    Map<String, Object> getToken(String apiKey);
}
