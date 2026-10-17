package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_role_permission")
@Schema(description = "角色权限关联实体")
public class RolePermission {

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "权限id")
    private Long permissionId;
}
