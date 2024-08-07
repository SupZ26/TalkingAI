package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Chat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    @Update("UPDATE db_chat SET topic = #{newTopic} WHERE username = #{username} AND topic = #{oldTopic}")
    int updateTopic(String username, String oldTopic, String newTopic);
}
