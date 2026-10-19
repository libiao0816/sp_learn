package com.libiao.lblearn1.service.sysUserRole;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.SysUserRole;
import com.libiao.lblearn1.mapper.SysUserRoleMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> {

    private final SysUserRoleMapper sysUserRoleMapper;

    public List<String> findRoleBySysUserId(Long sysUserId) {
        return sysUserRoleMapper.findRoleCodesBySysUserId(sysUserId);
    }
}
