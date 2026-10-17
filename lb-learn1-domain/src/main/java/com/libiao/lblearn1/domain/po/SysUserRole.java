package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_sys_user_role")
@Schema(description = "管理端用户角色关联实体")
public class SysUserRole {

    @Schema(description = "管理端用户id")
    private Long sysUserId;

    @Schema(description = "角色id")
    private Long roleId;
}
