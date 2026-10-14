package com.libiao.lblearn1.service.impl;

import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.enums.TokenTypeEnum;
import com.libiao.lblearn1.common.enums.UserErrorCodeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.domain.dto.user.UserLoginDTO;
import com.libiao.lblearn1.domain.dto.user.UserRegisterDTO;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.domain.vo.user.LoginUserVO;
import com.libiao.lblearn1.security.LoginUser;
import com.libiao.lblearn1.mapper.UserMapper;
import com.libiao.lblearn1.security.TokenBlackService;
import com.libiao.lblearn1.security.UserDetailsServiceImpl;
import com.libiao.lblearn1.service.AuthService;
import io.jsonwebtoken.Claims;
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

    private final UserMapper userMapper;

    private final TokenBlackService tokenBlackService;

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public LoginUserVO login(UserLoginDTO userLoginDTO) {
        // ★核心一行：把用户名密码交给框架去校验
        // 它会走 UserDetailsService（查库） + PasswordEncoder（比对）
        // 校验失败抛 BadCredentialsException → 由 GlobalExceptionHandler 兜住
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(), userLoginDTO.getPassword())); // 这个构造器 = 未认证状态

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> authList = loginUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        User userBuild = User.builder().id(loginUser.getId()).username(loginUser.getUsername()).build();

        String accessToken = JwtUtil.createToken(userBuild, jwtProperties.getSecret(), jwtProperties.getTtl(), authList,
                TokenTypeEnum.ACCESS_TOKEN.getType());
        String refreshToken = JwtUtil.createToken(userBuild, jwtProperties.getSecret(), jwtProperties.getRefreshTtl(),
                authList, TokenTypeEnum.REFRESH_TOKEN.getType());

        return LoginUserVO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public Integer register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int insert = userMapper.insert(user);

        if (insert != 1) {
            throw new BusinessException(UserErrorCodeEnum.REGISTER_FAILED.getCode(),
                    UserErrorCodeEnum.REGISTER_FAILED.getMsg());
        }

        return insert;
    }

    @Override
    public Boolean logout(String token) {
        Claims claims = JwtUtil.parseToken(token, jwtProperties.getSecret());
        tokenBlackService.addBlackList(claims.getId(), claims.getExpiration().getTime());
        return true;
    }

    @Override
    public String refreshToken(String refreshToken) {
        Claims claims = JwtUtil.parseToken(refreshToken, jwtProperties.getSecret());
        String tokenType = TokenTypeEnum.REFRESH_TOKEN.getType();
        if (!tokenType.equals(claims.get("tokenType"))) {
            throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED.getCode(),
                    ResultCodeEnum.AUTHENTICATION_FAILED.getMsg());
        }
        // 重新查一次权限，顺便让权限变更在刷新时生效
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(claims.get("username").toString());
        User user1 = new User();
        BeanUtils.copyProperties(loginUser, user1);
        List<String> authList = loginUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return JwtUtil.createToken(user1, jwtProperties.getSecret(), jwtProperties.getTtl(),
                authList,
                TokenTypeEnum.ACCESS_TOKEN.getType());
    }
}
