package com.libiao.lblearn1.service.clientAuth;

import com.libiao.lblearn1.common.properties.JwtProperties;
import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.domain.dto.user.ClientUserLoginDTO;
import com.libiao.lblearn1.domain.vo.user.LoginClientUserVO;
import com.libiao.lblearn1.security.clientUser.LoginClientUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientAuthService {

    private final JwtProperties jwtProperties;

    /** C 端认证管理器：@Qualifier 会在编译期被 Lombok 复制到构造器参数上，显式钉死注入目标 */
    @Qualifier("clientUserAuthenticationManager")
    private final AuthenticationManager clientUserAuthenticationManager;

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
                jwtProperties.getSecret(), jwtProperties.getAccessTtl(), authList);
        String refreshToken = JwtUtil.createRefreshToken(loginClientUser.getId(), loginClientUser.getUsername(),
                jwtProperties.getSecret(), jwtProperties.getRefreshTtl(), authList);

        return LoginClientUserVO.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
