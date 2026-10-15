package com.libiao.lblearn1.domain.security;

import com.libiao.lblearn1.domain.po.SysUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class LoginSysUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Integer status;
    private final List<GrantedAuthority> authorities; // 权限集合

    /**
     * 认证阶段用：带密码。
     * 参数用 Collection<? extends GrantedAuthority>，这样传 List<SimpleGrantedAuthority>
     * 也能编译通过（泛型不可变，写成 List<GrantedAuthority> 会报类型不匹配）。
     */
    public LoginSysUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        this.id = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.status = sysUser.getStatus();
        this.authorities = List.copyOf(authorities);
    }

    /** 验 token 阶段用：token 里没有密码，直接给 null */
    public LoginSysUser(Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = null;
        this.status = 1;
        this.authorities = List.copyOf(authorities);
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }

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
