package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.AddDepositVO;
import com.example.entity.vo.request.UserDetailsInfoVO;
import com.example.service.AccountDetailsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/accounts")
public class AccountDetailsController {

    @Resource
    AccountDetailsService accountDetailsService;


    /**
     * 给余额充值
     * @param vo
     * @return
     */
    @PostMapping("/addDeposit")
    public RestBean<String> addDeposit(@RequestBody AddDepositVO vo){
        if(accountDetailsService.addDeposit(vo) != null)
            return RestBean.success(accountDetailsService.addDeposit(vo));
        else
            return RestBean.failure(400,"充值失败");
    }

    /**
     * 查找用户的所有信息
     * @param username
     * @return 所有信息
     */
    @GetMapping("/findAllDetails")
    public RestBean<UserDetailsInfoVO> findAllAboutUser(@RequestParam String username){
        return RestBean.success(accountDetailsService.findAllAboutUser(username));
    }






}
