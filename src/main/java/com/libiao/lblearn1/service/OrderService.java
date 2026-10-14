package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.order.OrderPageDTO;
import com.libiao.lblearn1.domain.po.Order;
import com.libiao.lblearn1.domain.vo.order.OrderPageVO;

public interface OrderService extends IService<Order> {

    PageResult<OrderPageVO> selectPage(OrderPageDTO orderPageDTO);

    Boolean deleteById(Long id);
}
