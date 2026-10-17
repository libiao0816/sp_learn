package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_permission")
@Schema(description = "权限实体")
public class Permission {

    @Schema(description = "权限id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "权限编码")
    private String code;

    @Schema(description = "权限名称")
    private String name;

    /**
     * 权限类型：1-页面级 2-方法级 3-组件级（对应 PermissionTypeEnum）
     */
    @Schema(description = "权限类型：1-页面级 2-方法级 3-组件级")
    private Integer type;
}
