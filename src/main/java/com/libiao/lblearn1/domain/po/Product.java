package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@TableName("t_product")
public class Product {

    private Long id;

    private String name;

    private Long categoryId;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String image;

    private Short status;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Integer deleted;
}
