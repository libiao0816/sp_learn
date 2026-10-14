package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.cart.CartAddDTO;
import com.libiao.lblearn1.domain.dto.cart.CartPageDTO;
import com.libiao.lblearn1.domain.po.Cart;
import com.libiao.lblearn1.domain.vo.cart.CartPageVO;

import java.util.List;

public interface CartService extends IService<Cart> {

    int addCart(CartAddDTO cartAddDTO);

    int removeCart(List<Long> ids);

    PageResult<CartPageVO> getCartPage(CartPageDTO cartPageDTO);
}
