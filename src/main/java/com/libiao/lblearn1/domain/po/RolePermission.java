package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_role_permission")
public class RolePermission {
    private Long roleId;

    private Long permissionId;
}
