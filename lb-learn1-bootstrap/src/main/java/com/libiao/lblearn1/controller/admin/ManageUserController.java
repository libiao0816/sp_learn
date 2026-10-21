package com.libiao.lblearn1.controller.admin;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.user.UserPageDTO;
import com.libiao.lblearn1.domain.po.User;
import com.libiao.lblearn1.service.clientUser.ClientUserService;

@RestController
@RequestMapping("/admin/manageUser")
@RequiredArgsConstructor
public class ManageUserController {

    private final ClientUserService clientUserService;

    @GetMapping("/page")
    @PreAuthorize("hasRole('admin')")
    public Result<PageResult<User>> getUserPage(UserPageDTO userPageDTO) {
        return Result.Success(clientUserService.getUserPage(userPageDTO));
    }
}
