package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * C 端用户认证方式表：多端带来多种登录方式（手机号、邮箱、用户名、微信、Apple 等）。
 * 一个用户可以绑定多种登录方式。
 */
@Data
@TableName("user_auth")
@Schema(description = "C端用户认证方式表")
public class UserAuth {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 关联 user.id */
    @Schema(description = "关联 user.id")
    private Long userId;

    /** 登录类型：phone / email / username / wechat_mini / wechat_app / apple */
    @Schema(description = "登录类型：phone / email / username / wechat_mini / wechat_app / apple")
    private String identityType;

    /** 手机号、邮箱、openid、用户名 */
    @Schema(description = "手机号、邮箱、openid、用户名")
    private String identifier;

    /** 密码哈希；验证码登录可为空 */
    @Schema(description = "密码哈希；验证码登录可为空")
    private String credential;

    /** 微信 unionid，用于打通小程序和 App */
    @Schema(description = "微信 unionid，用于打通小程序和 App")
    private String unionId;

    /** 是否已验证 */
    @Schema(description = "是否已验证")
    private Integer verified;

    /** 绑定时间 */
    @Schema(description = "绑定时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
