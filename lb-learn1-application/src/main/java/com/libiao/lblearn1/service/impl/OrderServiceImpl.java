package com.libiao.lblearn1.service.impl;

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
import com.libiao.lblearn1.domain.security.LoginUser;
import com.libiao.lblearn1.service.OrderItemService;
import com.libiao.lblearn1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderItemService orderItemService;

    @Override
    public Boolean deleteById(Long id) {
        boolean result = removeById(id);
        if(!result){
            throw new BusinessException(OrderCodeEnum.ORDER_DELETE_FAILED.getCode(), OrderCodeEnum.ORDER_DELETE_FAILED.getMsg());
        }
        return Boolean.TRUE;
    }

    @Override
    public PageResult<OrderPageVO> selectPage(OrderPageDTO orderPageDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<Order> orderPage = lambdaQuery()
                .select(Order::getId)
                .eq(Order::getUserId, loginUser.getId())
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
}
