package com.libiao.lblearn1.service.admin.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.SysUser;
import com.libiao.lblearn1.mapper.SysUserMapper;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.security.sysUser.LoginSysUser;
import com.libiao.lblearn1.service.admin.SysUserRoleService;
import com.libiao.lblearn1.service.admin.SysUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;

    private final JwtProperties jwtProperties;

    private final SysUserRoleService sysUserRoleService;

    @Override
    public Boolean sysUserIsExist(String username) {
        return null;
    }

    @Override
    public SysUser getSysUserDetail() {
        LoginSysUser principal = (LoginSysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return lambdaQuery()
                .eq(SysUser::getId, principal.getId())
                .one();
    }
}
