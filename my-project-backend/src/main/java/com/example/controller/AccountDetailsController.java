package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.AddDepositVO;
import com.example.entity.vo.request.UserDetailsInfoVO;
import com.example.service.AccountDetailsService;
import com.example.validation.ValidString;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
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

    /**
     * 修改账号名
     * @param oldUsername
     * @param newUsername
     * @return
     */
    @PutMapping("/updateUsername")
    public RestBean<String> updateUsername(@RequestParam("oldUsername") @ValidString(minLength = 1,maxLength = 10) String oldUsername,
                                           @RequestParam("newUsername") @ValidString(minLength = 1,maxLength = 10) String newUsername){
        return accountDetailsService.updateUsername(oldUsername, newUsername) == 1
                ? RestBean.success("账号名修改成功")
                : RestBean.failure(500,"账号修改失败");
    }

    @PutMapping("/updatePassword")
    public RestBean<String> updatePassword(@RequestParam("username")@ValidString(minLength = 1,maxLength = 10) String username,
                                           @RequestParam("oldPassword")@ValidString(minLength = 1,maxLength = 20) String oldPassword,
                                           @RequestParam("newPassword")@ValidString(minLength = 1,maxLength = 20) String newPassword){
        return accountDetailsService.updatePassword(username, oldPassword, newPassword) == 1
                ? RestBean.success("密码修改成功")
                : RestBean.failure(500,"账号修改失败");
    }
}
