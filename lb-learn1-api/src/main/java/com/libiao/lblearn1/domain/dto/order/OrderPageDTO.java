package com.libiao.lblearn1.domain.dto.order;

import com.libiao.lblearn1.common.enums.OrderStatusEnum;
import com.libiao.lblearn1.domain.dto.page.PageQuery;
import lombok.Data;

@Data
public class OrderPageDTO extends PageQuery {

    private Long userId;

    private Long orderId;

    private OrderStatusEnum status;

    private Short createTimeOrderBy;
}
