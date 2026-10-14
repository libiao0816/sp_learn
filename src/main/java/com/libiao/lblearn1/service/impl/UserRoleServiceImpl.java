package com.libiao.lblearn1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.UserRole;
import com.libiao.lblearn1.mapper.UserRoleMapper;
import com.libiao.lblearn1.service.UserRoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    public List<String> findRoleByUserId(Long userId) {
        return userRoleMapper.findRoleCodesByUserId(userId);
    }
}
