package com.libiao.lblearn1.security.clientUser;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginClientUserDetailServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User findUser = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getAccount, account)
        );

        if(findUser == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        return new LoginClientUser(findUser);
    }
}
