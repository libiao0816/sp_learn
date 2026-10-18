package com.libiao.lblearn1.service.admin;

import com.libiao.lblearn1.domain.dto.sysUser.SysUserLoginDTO;
import com.libiao.lblearn1.domain.dto.sysUser.SysUserRegisterDTO;
import com.libiao.lblearn1.domain.vo.sysuser.LoginSysUserVO;

public interface AuthService {

    LoginSysUserVO login(SysUserLoginDTO sysUserLoginDTO);

    Integer register(SysUserRegisterDTO sysUserRegisterDTO);

    Boolean logout(String token);

    String refreshToken(String token);
}
