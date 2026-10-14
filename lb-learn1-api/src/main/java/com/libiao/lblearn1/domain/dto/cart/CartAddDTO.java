package com.libiao.lblearn1.domain.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartAddDTO {

    /**
     * 商品id
     */
    @Schema(description = "商品id")
    @NotNull(message = "商品id不能为空")
    private Long productId;

    @Schema(description = "数量")
    @NotNull(message = "数量不能为空")
    private Integer quantity;
}
