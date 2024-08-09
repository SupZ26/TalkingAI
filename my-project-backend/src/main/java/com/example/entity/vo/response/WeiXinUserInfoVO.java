package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeiXinUserInfoVO {
    String openid;
    String nickname;
    int sex;
    String province;
    String city;
    String country;
    String headimgurl;
    private List<String> privilege;
    String unionid;
}
