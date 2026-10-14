package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.domain.po.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    List<String> findRoleByUserId(Long userId);
}
