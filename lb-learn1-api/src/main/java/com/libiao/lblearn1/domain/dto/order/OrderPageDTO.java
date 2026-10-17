package com.libiao.lblearn1.domain.dto.order;

import com.libiao.lblearn1.common.enums.OrderStatusEnum;
import com.libiao.lblearn1.domain.dto.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单分页查询参数")
public class OrderPageDTO extends PageQuery {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "订单状态")
    private OrderStatusEnum status;

    @Schema(description = "创建时间排序：0-升序 1-降序，不传默认降序")
    private Short createTimeOrderBy;
}
