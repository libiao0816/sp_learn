package com.libiao.lblearn1.domain.vo.order;

import com.libiao.lblearn1.domain.po.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageVO {
    private Long id;

    private String orderNo;

    private Long userId;

    private BigDecimal totalAmount;

    private Short status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<OrderItem> orderItems;
}
