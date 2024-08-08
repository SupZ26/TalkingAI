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

    //查询用户身份的全部信息
    UserDetailsInfoVO findAllAboutUser(String username);

    //更新token余额
    int updateToken(String username,double remaining);
}
