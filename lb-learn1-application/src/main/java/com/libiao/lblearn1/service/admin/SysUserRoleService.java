package com.libiao.lblearn1.service.admin;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.domain.po.SysUserRole;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {

    List<String> findRoleBySysUserId(Long sysUserId);
}
