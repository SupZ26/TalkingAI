package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeiXinAccessTokenVO {
    String access_token;
    String expires_in;
    String refresh_token;
    String openid;
    String scope;
    String unionid;
}
