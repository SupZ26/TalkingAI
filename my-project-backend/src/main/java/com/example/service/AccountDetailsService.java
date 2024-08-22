package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.AddDepositVO;
import com.example.entity.vo.request.UserDetailsInfoVO;
import org.springframework.stereotype.Service;

@Service
public interface AccountDetailsService extends IService<Account> {

    //充值余额
    String addDeposit(AddDepositVO addDepositVO);

    //根据姓名查询用户身份的全部信息
    UserDetailsInfoVO findAllAboutUser(String username);

    //更新token余额
    int updateToken(String username,double remaining);

    //将用户和微信进行绑定
    int bondWithWeiXin(String username,String openId);

    //查询是否有绑定的微信，如果有直接登录(这里仅能象征性代表微信绑定登录)
    boolean isPresentOpenId(String username);

    //修改用户名
    int updateUsername(String oldUsername,String newUsername);

    //修改密码
    int updatePassword(String username,String oldPassword,String newPassword);


}
