package com.libiao.lblearn1.controller.client;

import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.order.OrderPageDTO;
import com.libiao.lblearn1.domain.vo.order.OrderPageVO;
import com.libiao.lblearn1.service.order.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("clientOrderController")
@RequestMapping("/client/order")
@Tag(name = "C端订单controller", description = "C端订单：分页查、查详情")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/page")
    @Operation(summary = "订单分页", description = "查询当前登录用户的订单分页")
    public Result<PageResult<OrderPageVO>> selectPage(@RequestBody OrderPageDTO orderPageDTO) {
        return Result.Success(orderService.selectPage(orderPageDTO));
    }

    @PostMapping("/detail")
    @Operation(summary = "订单详情", description = "查询当前登录用户的订单详情")
    public Result<OrderPageVO> selectDetailById(
            @Parameter(name = "id", description = "订单id", required = true,
                    schema = @Schema(type = "integer", example = "1"))
            @RequestParam Long id) {
        return Result.Success(orderService.selectDetailById(id));
    }
}
