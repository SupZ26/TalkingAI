package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 数据库中的用户信息
 */
@Data
@TableName("db_account")
@AllArgsConstructor
public class Account implements BaseData {
    @TableId(type = IdType.AUTO)
    Integer id;
    @Size(min = 1,max = 10,message = "姓名长度范围为1-10")
    String username;
    @Size(min = 1,max = 20,message = "密码长度范围为1-20")
    String password;
    String email;
    String role;
    @TableField("registerTime")
    Date registerTime;
    double deposit;
    double token;
    @TableField("openId")
    String openId;
    @TableField("giteeId")
    Integer giteeId;
}
