package com.libiao.lblearn1.domain.vo.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginUserVO {

    private String accessToken;

    private String refreshToken;
}
