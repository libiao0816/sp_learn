package com.libiao.lblearn1.controller;

import com.libiao.lblearn1.common.utils.JwtUtil;
import com.libiao.lblearn1.domain.vo.sysuser.LoginSysUserVO;
import com.libiao.lblearn1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.sysuser.SysUserLoginDTO;
import com.libiao.lblearn1.domain.dto.sysuser.SysUserRegisterDTO;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuhController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginSysUserVO> login(@RequestBody SysUserLoginDTO sysUserLoginDTO) {
        LoginSysUserVO token = authService.login(sysUserLoginDTO);
        return Result.Success(token);
    }
    
    @PostMapping("/register")
    public Result<Integer> register(@RequestBody SysUserRegisterDTO sysUserRegisterDTO) {
        Integer insert = authService.register(sysUserRegisterDTO);
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
