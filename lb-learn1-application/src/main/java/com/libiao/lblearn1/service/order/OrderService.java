package com.libiao.lblearn1.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.enums.OrderCodeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.order.OrderPageDTO;
import com.libiao.lblearn1.domain.po.Order;
import com.libiao.lblearn1.domain.po.OrderItem;
import com.libiao.lblearn1.domain.vo.order.OrderPageVO;
import com.libiao.lblearn1.mapper.OrderMapper;
import com.libiao.lblearn1.security.util.LoginUserUtils;
import com.libiao.lblearn1.service.orderItem.OrderItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    private final OrderItemService orderItemService;

    public Boolean deleteById(Long id) {
        boolean result = removeById(id);
        if(!result){
            throw new BusinessException(OrderCodeEnum.ORDER_DELETE_FAILED.getCode(), OrderCodeEnum.ORDER_DELETE_FAILED.getMsg());
        }
        return Boolean.TRUE;
    }

    public PageResult<OrderPageVO> selectPage(OrderPageDTO orderPageDTO) {
        Long userId = LoginUserUtils.getCurrentUserId();

        Page<Order> orderPage = lambdaQuery()
                .select(Order::getId)
                .eq(Order::getUserId, userId)
                .eq(Objects.nonNull(orderPageDTO.getOrderId()), Order::getId, orderPageDTO.getOrderId())
                .eq(Objects.nonNull(orderPageDTO.getStatus()), Order::getStatus, orderPageDTO.getStatus())
                .orderBy(orderPageDTO.getCreateTimeOrderBy() != null,
                        Objects.equals(orderPageDTO.getCreateTimeOrderBy(), (short) 1),
                        Order::getCreateTime)
                .page(new Page<>(orderPageDTO.getPageNum(), orderPageDTO.getPageSize()));

        List<Long> orderIds = orderPage.getRecords().stream().map(Order::getId).collect(Collectors.toList());

        List<OrderItem> orderItemsByOrderIds = orderItemService.getOrderItemsByOrderIds(orderIds);
        Map<Long, List<OrderItem>> orderItemMap = orderItemsByOrderIds.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));

        List<OrderPageVO> voList = orderPage.getRecords().stream().map(order -> {
            OrderPageVO orderPageVO = new OrderPageVO();
            BeanUtils.copyProperties(order, orderPageVO);
            orderPageVO.setOrderItems(orderItemMap.getOrDefault(order.getId(), Collections.emptyList()));
            return orderPageVO;
        }).collect(Collectors.toList());

        return new PageResult<OrderPageVO>(orderPage.getTotal(), voList);
    }

    public OrderPageVO selectDetailById(Long id) {
        Long userId = LoginUserUtils.getCurrentUserId();
        // 只允许查自己的订单：id 相同但 userId 不匹配时 one() 返回 null
        Order order = lambdaQuery()
                .eq(Order::getId, id)
                .eq(Order::getUserId, userId)
                .one();
        if (Objects.isNull(order)) {
            throw new BusinessException(OrderCodeEnum.ORDER_NOT_FOUND.getCode(), OrderCodeEnum.ORDER_NOT_FOUND.getMsg());
        }
        OrderPageVO orderPageVO = new OrderPageVO();
        BeanUtils.copyProperties(order, orderPageVO);
        orderPageVO.setOrderItems(orderItemService.getOrderItemsByOrderIds(List.of(order.getId())));
        return orderPageVO;
    }
}
