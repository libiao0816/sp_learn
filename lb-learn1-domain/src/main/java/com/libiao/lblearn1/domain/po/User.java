package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * C 端用户主表：只存身份本身，所有端（PC / 小程序 / App）共用一个 user_id。
 */
@Data
@TableName("user")
public class User {

    /** 用户内部唯一 ID，主键 */
    private Long id;

    /** 对外用户编号，避免暴露自增 ID */
    private String userNo;

    /** 状态：1 正常，0 禁用，2 注销 */
    private Integer status;

    /** 首次注册来源：PC_WEB / MINI_PROGRAM / APP_IOS / APP_ANDROID */
    private String registerSource;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 性别 */
    private Integer gender;

    /** 最后登录时间，后台展示用 */
    private LocalDateTime lastLoginTime;

    /** 注册时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除：0 未删除，1 已删除 */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}
