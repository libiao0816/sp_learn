package com.libiao.lblearn1.controller.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.po.SysUser;
import com.libiao.lblearn1.service.SysUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/sysUser")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @PostMapping("/detail")
    public Result<SysUser> detail() {
        SysUser sysUser = sysUserService.getSysUserDetail();
        return Result.Success(sysUser);
    }
}
