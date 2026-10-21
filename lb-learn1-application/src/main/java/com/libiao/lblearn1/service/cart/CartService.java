package com.libiao.lblearn1.service.cart;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.cart.CartAddDTO;
import com.libiao.lblearn1.domain.dto.cart.CartPageDTO;
import com.libiao.lblearn1.domain.po.Cart;
import com.libiao.lblearn1.domain.vo.cart.CartPageVO;
import com.libiao.lblearn1.mapper.CartMapper;
import com.libiao.lblearn1.security.util.LoginUserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService extends ServiceImpl<CartMapper, Cart> {

    private final CartMapper cartMapper;

    public int addCart(CartAddDTO cartAddDTO) {
        Long userId = LoginUserUtils.getCurrentUserId();
        int result = 0;
        try {
            Cart cart = new Cart();
            BeanUtils.copyProperties(cartAddDTO, cart);
            cart.setUserId(userId);
            result = cartMapper.insert(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    public int removeCart(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        // 按 id 删除，并限定只删当前登录用户自己的购物车
        return cartMapper.delete(new LambdaQueryWrapper<Cart>()
                .in(Cart::getId, ids)
                .eq(Cart::getUserId, LoginUserUtils.getCurrentUserId()));
    }

    public PageResult<CartPageVO> getCartPage(CartPageDTO cartPageDTO) {
        Page<CartPageVO> page = cartMapper.getCartPage(
                new Page<>(cartPageDTO.getPageNum(), cartPageDTO.getPageSize()),
                cartPageDTO,
                LoginUserUtils.getCurrentUserId()
        );
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
