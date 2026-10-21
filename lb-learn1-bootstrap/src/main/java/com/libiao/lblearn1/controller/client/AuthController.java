package com.libiao.lblearn1.controller.client;

import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.user.ClientUserLoginDTO;
import com.libiao.lblearn1.domain.dto.user.ClientUserRegisterDTO;
import com.libiao.lblearn1.domain.vo.user.LoginClientUserVO;
import com.libiao.lblearn1.service.clientAuth.ClientAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientAuthService clientAuthService;

    @PostMapping("/login")
    public Result<LoginClientUserVO> login(@RequestBody ClientUserLoginDTO clientUserLoginDTO) {
        LoginClientUserVO token = clientAuthService.login(clientUserLoginDTO);
        return Result.Success(token);
    }

    @PostMapping("/register")
    public Result<Integer> register(@Valid @RequestBody ClientUserRegisterDTO clientUserRegisterDTO) {
        Integer insert = clientAuthService.register(clientUserRegisterDTO);
        return Result.Success(insert);
    }
}
