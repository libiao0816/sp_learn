package com.libiao.lblearn1.service.sysUser;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.SysUser;
import com.libiao.lblearn1.mapper.SysUserMapper;
import com.libiao.lblearn1.security.sysUser.LoginSysUser;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    public Boolean sysUserIsExist(String username) {
        return null;
    }

    public SysUser getSysUserDetail() {
        LoginSysUser principal = (LoginSysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return lambdaQuery()
                .eq(SysUser::getId, principal.getId())
                .one();
    }
}
