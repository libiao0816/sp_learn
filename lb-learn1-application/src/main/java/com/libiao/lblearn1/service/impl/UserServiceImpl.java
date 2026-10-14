package com.libiao.lblearn1.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.mapper.UserMapper;
import com.libiao.lblearn1.domain.security.LoginUser;
import com.libiao.lblearn1.service.UserRoleService;
import com.libiao.lblearn1.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final JwtProperties jwtProperties;

    private final UserRoleService userRoleService;

    @Override
    public Boolean userIsExist(String username) {
        return null;
    }

    @Override
    public User getUserDetail() {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return lambdaQuery()
                .eq(User::getId, principal.getId())
                .one();
    }
}
