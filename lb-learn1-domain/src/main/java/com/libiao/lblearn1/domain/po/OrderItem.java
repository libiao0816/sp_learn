package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order_item")
public class OrderItem {
    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer quantity;

    private BigDecimal subtotal;
}
