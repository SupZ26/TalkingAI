package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.AccountDetailsService;
import com.example.service.TokenService;
import com.example.service.impl.TokenServiceImpl;
import com.example.utils.Const;
import jakarta.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class OpenKeyController {


    @Autowired
    private TokenService tokenService;


    /**
     * 官网查询Tokoen使用情况
     * @return
     */
    @GetMapping("/getTokenDetails")
    public String getTokenDetails(@RequestAttribute(Const.ATTR_USER_ID)int id) {
        return tokenService.getToken(id);
    }

    /**
     * 为新用户绑定token
     * @return
     */
    @GetMapping("/bindToken")
    public RestBean<Void> bindToken(@RequestAttribute(Const.ATTR_USER_ID)int id){
        tokenService.bindToken(id);
        return RestBean.success();
    }

    /**
     * 使用余额买key
     * @return
     */
    @PutMapping("/buyKeyByDeposit/{username}")
    public RestBean<String> buyKeyByDeposit(@RequestAttribute(Const.ATTR_USER_ID)int id){
        tokenService.buyKeyByDeposit(id);
        return RestBean.success("购买成功");
    }

    /**
     * 使用支付宝支付购买key
     * @param username
     * @return
     */
    @PutMapping("/buyKeyByAlipay/{username}")
    public RestBean<String> buyKeyByAlipay(@PathVariable String username){
        tokenService.buyKeyByAlipay(username);
        return RestBean.success("购买成功");
    }

    /**
     * 检查用户是否已经绑定了token
     */
    @GetMapping("/isBindWithToken/{username}")
    public RestBean<Boolean> isBindWithToken(@PathVariable String username){
        if(tokenService.isBindWithToken(username))
            return RestBean.success(true);
        else
            return RestBean.success(false);
    }

}
