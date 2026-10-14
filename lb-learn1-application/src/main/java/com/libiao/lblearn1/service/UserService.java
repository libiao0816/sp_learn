package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.domain.dto.user.UserLoginDTO;
import com.libiao.lblearn1.domain.dto.user.UserRegisterDTO;
import com.libiao.lblearn1.domain.po.User;

public interface UserService extends IService<User> {

    User getUserDetail();

    Boolean userIsExist(String username);
}
