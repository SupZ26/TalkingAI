package com.example.service;


import java.util.Map;

public interface TokenService {
    /**
     * 从官网查询Token的具体信息
     */
    Map<String, Object> getToken(String username);

    //为新用户绑定token
    void bindToken(String username);

    //查询用户绑定的tokenId
    String getTokenIdByUsername(String username);
}
