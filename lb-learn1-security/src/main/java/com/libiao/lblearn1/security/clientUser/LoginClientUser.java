package com.libiao.lblearn1.security.clientUser;

import com.libiao.lblearn1.domain.po.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class LoginClientUser implements UserDetails {

    private final Long id;
    private final String account;
    private final String password;
    private final Integer status;

    public LoginClientUser(Long id, String account, String password, Integer status) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.status = status;
    }

    public LoginClientUser(User user) {
        this.id = user.getId();
        this.account = user.getAccount();
        this.password = user.getPassword();
        this.status = user.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    // 下面四个是"账号状态"。哪个返回 false，认证阶段就会抛对应异常。
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }
    @Override public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
    @Override public boolean isEnabled()               { return status != null && status == 1; } // 禁用账号走这里
}
