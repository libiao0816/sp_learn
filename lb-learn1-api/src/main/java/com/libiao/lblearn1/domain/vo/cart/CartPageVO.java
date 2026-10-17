package com.libiao.lblearn1.domain.vo.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Schema(description = "购物车分页响应")
public class CartPageVO {

    @Schema(description = "购物车id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "商品id")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "商品单价")
    private BigDecimal price;

    @Schema(description = "库存")
    private Integer stock;
}
