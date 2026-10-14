package com.libiao.lblearn1.controller;

import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.domain.vo.user.LoginUserVO;
import com.libiao.lblearn1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.user.UserLoginDTO;
import com.libiao.lblearn1.domain.dto.user.UserRegisterDTO;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuhController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginUserVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        LoginUserVO token = authService.login(userLoginDTO);
        return Result.Success(token);
    }
    
    @PostMapping("/register")
    public Result<Integer> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        Integer insert = authService.register(userRegisterDTO);
        return Result.Success(insert);
    }

    @PostMapping("/logout")
    public Result<Boolean> logout(@RequestHeader("Authorization") String token) {
        String subStrToken = JwtUtil.subStrToken(token);
        Boolean logout = authService.logout(subStrToken);
        return Result.Success(logout);
    }

    @PostMapping("/refresh")
    public Result<String> refresh(@RequestBody Map<String, String> map) {
        String accessToken = authService.refreshToken(map.get("refreshToken"));
        return Result.Success(accessToken);
    }
    
}
