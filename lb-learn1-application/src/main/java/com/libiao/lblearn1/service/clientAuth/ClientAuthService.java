package com.libiao.lblearn1.service.clientAuth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import com.libiao.lblearn1.common.enums.SysUserErrorCodeEnum;
import com.libiao.lblearn1.common.enums.TokenTypeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.common.model.TokenPayload;
import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.domain.dto.user.ClientUserLoginDTO;
import com.libiao.lblearn1.domain.dto.user.ClientUserRegisterDTO;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.domain.vo.user.LoginClientUserVO;
import com.libiao.lblearn1.mapper.UserMapper;
import com.libiao.lblearn1.security.clientUser.LoginClientUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientAuthService {

    private final JwtProperties jwtProperties;

    /** C 端认证管理器：@Qualifier 会在编译期被 Lombok 复制到构造器参数上，显式钉死注入目标 */
    @Qualifier("clientUserAuthenticationManager")
    private final AuthenticationManager clientUserAuthenticationManager;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public Integer register(ClientUserRegisterDTO clientUserRegisterDTO) {
        String account = clientUserRegisterDTO.getAccount().trim();

        Long exists = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getAccount, account));
        if (exists != null && exists > 0) {
            throw new BusinessException(SysUserErrorCodeEnum.USERNAME_ALREADY_EXISTS.getCode(), "账号已存在");
        }

        User user = new User();
        user.setAccount(account);
        user.setPassword(passwordEncoder.encode(clientUserRegisterDTO.getPassword()));
        // user_no 为必填且唯一，注册时生成一次对外编号
        user.setUserNo(UUID.randomUUID().toString().replace("-", ""));
        user.setStatus(1);
        user.setRegisterSource(clientUserRegisterDTO.getRegisterSource());

        int insert = userMapper.insert(user);
        if (insert != 1) {
            throw new BusinessException(SysUserErrorCodeEnum.REGISTER_FAILED.getCode(),
                    SysUserErrorCodeEnum.REGISTER_FAILED.getMsg());
        }
        return insert;
    }

    public LoginClientUserVO login(ClientUserLoginDTO clientUserLoginDTO) {
        // ★核心一行：把账号密码交给框架去校验
        // 它会走 LoginClientUserDetailServiceImpl（查 user 表） + PasswordEncoder（比对）
        // 校验失败抛 BadCredentialsException → 由 GlobalExceptionHandler 兜住
        Authentication authentication = clientUserAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        clientUserLoginDTO.getAccount(), clientUserLoginDTO.getPassword())); // 这个构造器 = 未认证状态

        LoginClientUser loginClientUser = (LoginClientUser) authentication.getPrincipal();

        // C 端暂时不涉及角色权限，token 里的权限列表传空
        List<String> authList = List.of();

        String accessToken = JwtUtil.createAccessToken(loginClientUser.getId(), loginClientUser.getUsername(),
                jwtProperties.getClient().getSecret(), jwtProperties.getClient().getAccessTtl(), authList);
        String refreshToken = JwtUtil.createRefreshToken(loginClientUser.getId(), loginClientUser.getUsername(),
                jwtProperties.getClient().getSecret(), jwtProperties.getClient().getRefreshTtl(), authList);

        return LoginClientUserVO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    /**
     * 用 refreshToken 换新的 accessToken
     * C 端不涉及角色权限，直接沿用 token 里的身份，无需重查库刷新权限
     */
    public String refreshToken(String refreshToken) {
        TokenPayload payload = JwtUtil.parseToken(refreshToken, jwtProperties.getClient().getSecret());
        if (!TokenTypeEnum.REFRESH_TOKEN.getType().equals(payload.tokenType())) {
            throw new BusinessException(ResultCodeEnum.AUTHENTICATION_FAILED.getCode(),
                    ResultCodeEnum.AUTHENTICATION_FAILED.getMsg());
        }
        return JwtUtil.createAccessToken(payload.userId(), payload.username(), jwtProperties.getClient().getSecret(),
                jwtProperties.getClient().getAccessTtl(), List.of());
    }
}
