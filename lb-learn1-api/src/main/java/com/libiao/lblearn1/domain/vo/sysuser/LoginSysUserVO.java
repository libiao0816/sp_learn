package com.libiao.lblearn1.domain.vo.sysuser;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginSysUserVO {

    private String accessToken;

    private String refreshToken;
}
