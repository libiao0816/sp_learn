package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.user.UserPageDTO;
import com.libiao.lblearn1.domain.po.User;

public interface UserService extends IService<User> {

    PageResult<User> getUserPage(UserPageDTO userPageDTO);
}
