package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_category")
@Schema(description = "商品分类实体")
public class Category {

    @Schema(description = "分类id")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "排序值")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
}
