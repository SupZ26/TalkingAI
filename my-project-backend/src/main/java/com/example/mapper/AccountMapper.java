package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    @Update("UPDATE db_account SET token = #{remaining} WHERE username = #{username}")
    int updateToken(String username, double remaining);
}
