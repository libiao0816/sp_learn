package com.libiao.lblearn1.service.admin.impl;

import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.enums.SysUserErrorCodeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.domain.dto.sysuser.SysUserLoginDTO;
import com.libiao.lblearn1.domain.dto.sysuser.SysUserRegisterDTO;
import com.libiao.lblearn1.domain.po.SysUser;
import com.libiao.lblearn1.domain.vo.sysuser.LoginSysUserVO;
import com.libiao.lblearn1.mapper.SysUserMapper;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.common.model.TokenPayload;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.redis.TokenBlackService;
import com.libiao.lblearn1.common.enums.TokenTypeEnum;
import com.libiao.lblearn1.security.sysUser.LoginSysUser;
import com.libiao.lblearn1.security.sysUser.SysUserDetailsServiceImpl;
import com.libiao.lblearn1.service.admin.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProperties jwtProperties;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final SysUserMapper sysUserMapper;

    private final TokenBlackService tokenBlackService;

    private final SysUserDetailsServiceImpl sysUserDetailsService;

    @Override
    public LoginSysUserVO login(SysUserLoginDTO sysUserLoginDTO) {
        // ★核心一行：把用户名密码交给框架去校验
        // 它会走 UserDetailsService（查库） + PasswordEncoder（比对）
        // 校验失败抛 BadCredentialsException → 由 GlobalExceptionHandler 兜住
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        sysUserLoginDTO.getUsername(), sysUserLoginDTO.getPassword())); // 这个构造器 = 未认证状态

        LoginSysUser loginSysUser = (LoginSysUser) authentication.getPrincipal();
        List<String> authList = loginSysUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        String accessToken = JwtUtil.createAccessToken(loginSysUser.getId(), loginSysUser.getUsername(),
                jwtProperties.getSecret(), jwtProperties.getAccessTtl(), authList);
        String refreshToken = JwtUtil.createRefreshToken(loginSysUser.getId(), loginSysUser.getUsername(),
                jwtProperties.getSecret(), jwtProperties.getRefreshTtl(), authList);

        return LoginSysUserVO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public Integer register(SysUserRegisterDTO sysUserRegisterDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserRegisterDTO, sysUser);
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        int insert = sysUserMapper.insert(sysUser);

        if (insert != 1) {
            throw new BusinessException(SysUserErrorCodeEnum.REGISTER_FAILED.getCode(),
                    SysUserErrorCodeEnum.REGISTER_FAILED.getMsg());
        }

        return insert;
    }

    @Override
    public Boolean logout(String token) {
        TokenPayload payload = JwtUtil.parseToken(token, jwtProperties.getSecret());
        tokenBlackService.addBlackList(payload.jti(), payload.expirationMillis());
        return true;
    }

    @Override
    public String refreshToken(String refreshToken) {
        TokenPayload payload = JwtUtil.parseToken(refreshToken, jwtProperties.getSecret());
        String tokenType = TokenTypeEnum.REFRESH_TOKEN.getType();
        if (!tokenType.equals(payload.tokenType())) {
            throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED.getCode(),
                    ResultCodeEnum.AUTHENTICATION_FAILED.getMsg());
        }
        // 重新查一次权限，顺便让权限变更在刷新时生效
        LoginSysUser loginSysUser = (LoginSysUser) sysUserDetailsService.loadUserByUsername(payload.username());
        List<String> authList = loginSysUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return JwtUtil.createAccessToken(loginSysUser.getId(), loginSysUser.getUsername(), jwtProperties.getSecret(),
                jwtProperties.getAccessTtl(), authList);
    }
}
