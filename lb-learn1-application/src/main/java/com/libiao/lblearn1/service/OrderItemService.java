package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.domain.po.OrderItem;

import java.util.List;

public interface OrderItemService extends IService<OrderItem> {

    List<OrderItem> getOrderItemsByOrderIds(List<Long> orderIds);
}
