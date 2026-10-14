package com.libiao.lblearn1.mapper;

import com.libiao.lblearn1.domain.po.User;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
}   
