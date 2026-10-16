package com.libiao.lblearn1.service.admin.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.SysUserRole;
import com.libiao.lblearn1.mapper.SysUserRoleMapper;
import com.libiao.lblearn1.service.admin.SysUserRoleService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<String> findRoleBySysUserId(Long sysUserId) {
        return sysUserRoleMapper.findRoleCodesBySysUserId(sysUserId);
    }
}
