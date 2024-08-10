package com.example.service.impl;

import com.example.service.WeiXinAuthService;
import com.jayway.jsonpath.JsonPath;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class WeiXinAuthServiceImpl implements WeiXinAuthService {
    @Value("${WeiXin.appid}")
    String appId;
    @Value("${WeiXin.login.access_token_url}")
    String accessTokenUrl;
    @Value("${WeiXin.app_secret}")
    String appSecret;
    @Value("${WeiXin.login.get_user_info_url}")
    String userInfoUrl;
    @Resource
    RestTemplate restTemplate;



    /**
     * 获取access_token，进而获取用户数据
     * @param code
     * @return
     */
    @Override
    public void callBack(String code) {

        //通过code获取access_token
        String url = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                accessTokenUrl, appId, appSecret, code);

        String response = restTemplate.getForObject(url, String.class);
        if(response.contains("access_token")){
            String accessToken = JsonPath.read(response, "$.access_token");
            getUserInfo(accessToken);
        }
        else if(response.contains("errcode")){
            throw new RuntimeException(response);
        }else {
            throw new RuntimeException("access_token获取时发生了未知错误");
        }

    }

    /**
     * 通过accessToken获取微信用户信息
     * @param accessToken
     * @return
     */
    //http请求方式: GET
    //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
    public String getUserInfo(String accessToken){
        String url = String.format("%s?access_token=%s&openid=%s",
                userInfoUrl,accessToken,"OPENID");
        String response = restTemplate.getForObject(url, String.class);
        if(response.contains("openid")){
            String openId =  JsonPath.read(response, "$.openid");
            //把openid传给前端监听的地址
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(openId,headers);
            restTemplate.postForLocation("http://127.0.0.1:5173/",entity);
            return null;
        }
        else if(response.contains("errcode")){
            throw new RuntimeException(response);
        }else {
            throw new RuntimeException("获取微信用户信息时发生了未知错误");
        }
    }


}

