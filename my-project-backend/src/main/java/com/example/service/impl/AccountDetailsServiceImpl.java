package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.AddDepositVO;
import com.example.entity.vo.request.UserDetailsInfoVO;
import com.example.mapper.AccountMapper;
import com.example.service.AccountDetailsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountDetailsService {

    @Resource
    AccountMapper accountMapper;

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
     * @param username  用户姓名
     * @return  用户所有信息
     */
    @Override
    public UserDetailsInfoVO findAllAboutUser(String username) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Account account = accountMapper.selectOne(queryWrapper);
        if(account == null){
            throw new RuntimeException("用户姓名不存在");
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
}
