package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 储存用户gpt对话详细数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("db_chat")
public class Chat {
    @TableId("username")
    String username;
    @TableField("topic")
    String topic;
    @TableField("question")
    String question;
    @TableField("response")
    String response;
    @TableField("dialog_id")
    int dialog_id;
}
