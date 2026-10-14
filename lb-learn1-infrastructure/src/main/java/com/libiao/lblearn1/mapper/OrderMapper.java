package com.libiao.lblearn1.mapper;

import com.libiao.lblearn1.domain.po.Order;
import com.libiao.lblearn1.domain.vo.order.OrderPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
