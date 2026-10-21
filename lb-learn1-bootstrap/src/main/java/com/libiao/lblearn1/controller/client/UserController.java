package com.libiao.lblearn1.controller.client;

import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.service.clientUser.ClientUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/user")
@Tag(name = "C端用户controller", description = "C端用户：查详情")
@RequiredArgsConstructor
public class UserController {

    private final ClientUserService clientUserService;

    @PostMapping("/detail")
    @Operation(summary = "当前用户详情", description = "查询当前登录用户详情")
    public Result<User> detail() {
        return Result.Success(clientUserService.getCurrentUserDetail());
    }
}
