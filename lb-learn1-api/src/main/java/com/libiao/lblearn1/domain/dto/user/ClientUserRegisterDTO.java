package com.libiao.lblearn1.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "C端用户注册参数")
public class ClientUserRegisterDTO {

    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "注册来源：PC_WEB / MINI_PROGRAM / APP_IOS / APP_ANDROID")
    @NotBlank(message = "注册来源不能为空")
    private String registerSource;
}
