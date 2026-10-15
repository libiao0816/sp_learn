package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.domain.po.SysUser;

public interface SysUserService extends IService<SysUser> {

    SysUser getSysUserDetail();

    Boolean sysUserIsExist(String username);
}
