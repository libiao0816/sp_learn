package com.libiao.lblearn1.service;

import com.libiao.lblearn1.domain.dto.user.UserLoginDTO;
import com.libiao.lblearn1.domain.dto.user.UserRegisterDTO;
import com.libiao.lblearn1.domain.vo.user.LoginUserVO;

public interface AuthService {

    LoginUserVO login(UserLoginDTO userLoginDTO);

    Integer register(UserRegisterDTO userRegisterDTO);

    Boolean logout(String token);

    String refreshToken(String token);
}
