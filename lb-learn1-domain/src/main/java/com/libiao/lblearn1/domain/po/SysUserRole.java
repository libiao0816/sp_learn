package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_sys_user_role")
public class SysUserRole {

    private Long sysUserId;

    private Long roleId;
}
