package com.libiao.lblearn1.service.admin.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.user.UserPageDTO;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.mapper.UserMapper;
import com.libiao.lblearn1.service.admin.ManageUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManageUserServiceImpl extends ServiceImpl<UserMapper, User> implements ManageUserService {

    private final UserMapper userMapper;
    
    @Override
    public PageResult<User> getUserPage(UserPageDTO userPageDTO) {
        Page<User> page = lambdaQuery().page(new Page<>(userPageDTO.getPageNum(), userPageDTO.getPageSize()));
        return new PageResult<>(page.getTotal(), page.getRecords());
       }
}
