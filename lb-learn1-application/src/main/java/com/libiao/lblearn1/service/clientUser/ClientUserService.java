package com.libiao.lblearn1.service.clientUser;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.user.UserPageDTO;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.mapper.UserMapper;
import com.libiao.lblearn1.security.util.LoginUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientUserService extends ServiceImpl<UserMapper, User> {

    public PageResult<User> getUserPage(UserPageDTO userPageDTO) {
        Page<User> page = lambdaQuery().page(new Page<>(userPageDTO.getPageNum(), userPageDTO.getPageSize()));
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    public User getCurrentUserDetail() {
        Long userId = LoginUserUtils.getCurrentUserId();
        User user = lambdaQuery().eq(User::getId, userId).one();
        if (Objects.isNull(user)) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }
        user.setPassword(null); // 不对外暴露密码
        return user;
    }
}
