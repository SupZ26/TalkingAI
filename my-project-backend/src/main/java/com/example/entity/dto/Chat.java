package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Size;
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
    @TableField("username")
    @Size(min = 1,max = 10,message = "姓名长度范围为1-10")
    String username;
    @TableField("topic")
    String topic;
    @TableField("question")
    String question;
    @TableField("response")
    String response;
    @TableId(value = "dialog_id",type = IdType.AUTO)
    int dialog_id;

    public Chat(String username, String topic, String question, String response) {
        this.username = username;
        this.topic = topic;
        this.question = question;
        this.response = response;
    }
}
