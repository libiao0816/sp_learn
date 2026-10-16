package com.libiao.lblearn1.service.admin.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.OrderItem;
import com.libiao.lblearn1.mapper.OrderItemMapper;
import com.libiao.lblearn1.service.admin.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

    @Override
    public List<OrderItem> getOrderItemsByOrderIds(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(OrderItem::getOrderId, orderIds).list();
    }
}
