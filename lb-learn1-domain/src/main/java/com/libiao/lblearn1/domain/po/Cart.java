package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_cart")
@Schema(description = "购物车实体")
public class Cart {

    @Schema(description = "购物车id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "商品id")
    private Long productId;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
