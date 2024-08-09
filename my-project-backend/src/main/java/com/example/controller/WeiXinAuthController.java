package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.WeiXinAuthService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/VX")
public class WeiXinAuthController {
    @Resource
    WeiXinAuthService weiXinAuthService;


    @GetMapping ("/weiXinCheck")
    public String weiXinCheck(@RequestParam(value = "signature") String signature,@RequestParam(value = "timestamp") String timestamp,
                               @RequestParam(value = "nonce") String nonce,@RequestParam(value = "echostr") String echostr){
        //TODO
        /**
         * 1）将token、timestamp、nonce三个参数进行字典序排序
         *
         * 2）将三个参数字符串拼接成一个字符串进行sha1加密
         *
         * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         */
        System.out.println(signature);
        System.out.println(nonce);
        return echostr;

    }

    @GetMapping("/callback")
    public void callBack(@RequestParam String code,@RequestParam String state){
        weiXinAuthService.callBack(code);
    }







}
