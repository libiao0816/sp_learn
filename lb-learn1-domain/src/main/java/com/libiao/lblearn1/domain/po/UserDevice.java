package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * C 端用户设备/终端表：记录用户在各端（PC / 小程序 / App）的登录设备信息。
 * 端的信息放这里，不要放到用户主表。
 */
@Data
@TableName("user_device")
@Schema(description = "C端用户设备/终端表")
public class UserDevice {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 关联 user.id */
    @Schema(description = "关联 user.id")
    private Long userId;

    /** 设备类型：PC_WEB / MINI_PROGRAM / APP_IOS / APP_ANDROID */
    @Schema(description = "设备类型：PC_WEB / MINI_PROGRAM / APP_IOS / APP_ANDROID")
    private String deviceType;

    /** 设备唯一标识 */
    @Schema(description = "设备唯一标识")
    private String deviceId;

    /** App 推送 token */
    @Schema(description = "App 推送 token")
    private String pushToken;

    /** 该设备最后登录时间 */
    @Schema(description = "该设备最后登录时间")
    private LocalDateTime lastLoginTime;

    /** 最后登录 IP */
    @Schema(description = "最后登录 IP")
    private String lastLoginIp;

    /** 首次登录时间 */
    @Schema(description = "首次登录时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
