package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order_item")
@Schema(description = "订单明细实体")
public class OrderItem {

    @Schema(description = "明细id")
    private Long id;

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "商品id")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品单价")
    private BigDecimal productPrice;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "小计金额")
    private BigDecimal subtotal;
}
