package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
@Builder
public class User {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)         // 插入时自动填充（第7章）
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时都填充
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
