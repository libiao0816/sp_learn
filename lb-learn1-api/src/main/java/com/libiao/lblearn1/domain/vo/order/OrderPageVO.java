package com.libiao.lblearn1.domain.vo.order;

import com.libiao.lblearn1.domain.po.OrderItem;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "订单分页响应")
public class OrderPageVO {

    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消")
    private Short status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "订单明细列表")
    private List<OrderItem> orderItems;
}
