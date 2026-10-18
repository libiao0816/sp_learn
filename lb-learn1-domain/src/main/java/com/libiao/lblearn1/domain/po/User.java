package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * C 端用户主表：只存身份本身，所有端（PC / 小程序 / App）共用一个 user_id。
 */
@Data
@TableName("user")
@Schema(description = "C端用户主表")
public class User {

    /** 用户内部唯一 ID，主键 */
    @Schema(description = "用户内部唯一 ID，主键")
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    /** 对外用户编号，避免暴露自增 ID */
    @Schema(description = "对外用户编号，避免暴露自增 ID")
    private String userNo;

    /** 状态：1 正常，0 禁用，2 注销 */
    @Schema(description = "状态：1-正常 0-禁用 2-注销")
    private Integer status;

    /** 首次注册来源：PC_WEB / MINI_PROGRAM / APP_IOS / APP_ANDROID */
    @Schema(description = "首次注册来源：PC_WEB / MINI_PROGRAM / APP_IOS / APP_ANDROID")
    private String registerSource;

    /** 昵称 */
    @Schema(description = "昵称")
    private String nickname;

    /** 头像 */
    @Schema(description = "头像")
    private String avatar;

    /** 性别 */
    @Schema(description = "性别")
    private Integer gender;

    /** 最后登录时间，后台展示用 */
    @Schema(description = "最后登录时间，后台展示用")
    private LocalDateTime lastLoginTime;

    /** 注册时间 */
    @Schema(description = "注册时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}
