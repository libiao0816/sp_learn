package com.libiao.lblearn1.domain.vo.cart;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartPageVO {
    private Long id;

    private Long userId;

    private Long productId;
    
    private String productName;

    private Integer quantity;

    private BigDecimal price;

    private Integer stock;
}
