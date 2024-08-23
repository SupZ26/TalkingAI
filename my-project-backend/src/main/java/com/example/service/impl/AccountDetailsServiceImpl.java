package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.AddDepositVO;
import com.example.entity.vo.request.UpdatePasswordVO;
import com.example.entity.vo.request.UserDetailsInfoVO;
import com.example.mapper.AccountMapper;
import com.example.service.AccountDetailsService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Date;
import java.util.Optional;


@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountDetailsService {

    @Resource
    AccountMapper accountMapper;
    @Resource
    PasswordEncoder passwordEncoder;



    /**
     * 用户充值余额
     * @param addDepositVO 余额
     * @return  充值后的余额
     */
    @Override
    public String addDeposit(AddDepositVO addDepositVO) {
        String username = addDepositVO.getUsername();
        String input = addDepositVO.getInput();

        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        double deposit = accountMapper.selectOne(queryWrapper).getDeposit();
        double newDeposit = deposit + Double.parseDouble(input);

        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",username)
                .set("deposit",newDeposit);
        accountMapper.update(null,updateWrapper);

        return String.valueOf(newDeposit);

    }

    /**
     * 根据姓名查找用户的全部信息
     * @return  用户所有信息
     */
    @Override
    public UserDetailsInfoVO findAllAboutUser(int id) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Account account = accountMapper.selectOne(queryWrapper);
        if(account == null){
            throw new RuntimeException("用户不存在");
        }else {
            UserDetailsInfoVO userDetails = new UserDetailsInfoVO();
            userDetails = account.asViewObject(UserDetailsInfoVO.class);
            return userDetails;
        }
    }

    /**
     * 更新token余额
     * @param username
     * @param remaining
     * @return
     */
    @Override
    public int updateToken(String username, double remaining) {
        return accountMapper.updateToken(username, remaining);
    }

    /**
     * 将用户和微信进行绑定
     * @param username
     * @param openId
     * @return
     */
    @Override
    public int bondWithWeiXin(String username, String openId) {
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",username)
                .set("openId",openId);
        return accountMapper.update(null,updateWrapper);
    }

    /**
     * 查询是否有绑定的微信，如果有直接登录(这里仅能象征性代表微信绑定登录)
     * @param username
     * @return
     */
    @Override
    public boolean isPresentOpenId(String username) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Account account = accountMapper.selectOne(queryWrapper);
        Optional<String> optionalOpenId = Optional.ofNullable(account)
                .map(Account::getOpenId);
        return optionalOpenId.isPresent();
    }


    /**
     * 修改用户名
     * @param oldUsername
     * @param newUsername
     * @return
     */
    @Override
    @Transactional
    public int updateUsername(String oldUsername, String newUsername) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",newUsername);
        if(accountMapper.selectOne(queryWrapper) != null)
            throw new RuntimeException("用户名已经存在，无法修改");
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",oldUsername)
                .set("username",newUsername);
        return accountMapper.update(null,updateWrapper);
    }

    /**
     * 修改密码
     * @return
     */
    @Override
    public int updatePassword(int id, UpdatePasswordVO updatePasswordVO) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        if(!passwordEncoder.matches(updatePasswordVO.getOldPassword(), accountMapper.selectOne(queryWrapper).getPassword()))
            throw new RuntimeException("输入的原密码错误，请重新输入");
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("password",passwordEncoder.encode(updatePasswordVO.getNewPassword()));
        return accountMapper.update(null,updateWrapper);

    }


}
