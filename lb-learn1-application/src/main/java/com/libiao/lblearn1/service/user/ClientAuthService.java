package com.libiao.lblearn1.service.user;

import com.libiao.lblearn1.domain.dto.user.ClientUserLoginDTO;
import com.libiao.lblearn1.domain.vo.user.LoginClientUserVO;

public interface ClientAuthService {

    LoginClientUserVO login(ClientUserLoginDTO clientUserLoginDTO);
}
