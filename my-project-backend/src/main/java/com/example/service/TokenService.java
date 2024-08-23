package com.example.service;


import java.util.Map;

public interface TokenService {
    /**
     * 从官网查询Token的具体信息
     */
    String getToken(int id);

    //为新用户绑定token
    void bindToken(int id);

    //查询用户绑定的tokenId
    String getTokenIdByUsername(String username);

    //使用余额买key
    void buyKeyByDeposit(int id);

    //支付宝支付购买key
    void buyKeyByAlipay(String username);

    //检查用户是否已经绑定了Token
    boolean isBindWithToken(String username);
}
