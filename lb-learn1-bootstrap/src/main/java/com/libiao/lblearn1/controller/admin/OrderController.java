package com.libiao.lblearn1.controller.admin;

import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.order.OrderPageDTO;
import com.libiao.lblearn1.domain.vo.order.OrderPageVO;
import com.libiao.lblearn1.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/deleteById")
    @PreAuthorize("hasAuthority('order:delete')")
    public Result<Boolean> deleteById(@RequestParam Long id) {
        return Result.Success(orderService.deleteById(id));
    }

    @PostMapping("/page")
    public Result<PageResult<OrderPageVO>> page(@RequestBody OrderPageDTO orderPageDTO) {
        return Result.Success(orderService.selectPage(orderPageDTO));
    }
}
