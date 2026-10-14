package com.libiao.lblearn1.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.libiao.lblearn1.domain.po.Permission;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.mapper.RolePermissionMapper;
import com.libiao.lblearn1.mapper.UserMapper;
import com.libiao.lblearn1.mapper.UserRoleMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查库（和你手写版一模一样）
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        if (user == null) {
            // ★ 抛出这个异常，DaoAuthenticationProvider 默认会把它"翻译"成 BadCredentialsException，
            // 对外统一表现为"用户名或密码错误"，防止攻击者探测哪个用户名存在。
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        List<String> roleCodes = userRoleMapper.findRoleCodesByUserId(user.getId());

        List<Permission> permissions = rolePermissionMapper.findPermissionsByUserId(user.getId());

        // 权限组装
        List<GrantedAuthority> grantedAuthorities = Stream.concat(
                roleCodes.stream().map(i -> new SimpleGrantedAuthority("ROLE_" + i)),
                permissions.stream().map(i -> new SimpleGrantedAuthority(i.getCode())))
                .map(a -> (GrantedAuthority) a).toList();

        // 2. 组装权限。注意：hasRole('ADMIN') 实际匹配的是 "ROLE_ADMIN"，所以这里要加前缀！
        //List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new LoginUser(user, List.copyOf(grantedAuthorities));
    }
}
