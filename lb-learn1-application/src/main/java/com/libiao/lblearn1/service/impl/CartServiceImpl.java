package com.libiao.lblearn1.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.cart.CartAddDTO;
import com.libiao.lblearn1.domain.dto.cart.CartPageDTO;
import com.libiao.lblearn1.domain.po.Cart;
import com.libiao.lblearn1.domain.vo.cart.CartPageVO;
import com.libiao.lblearn1.mapper.CartMapper;
import com.libiao.lblearn1.domain.security.LoginUser;
import com.libiao.lblearn1.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    private final CartMapper cartMapper;

    @Override
    public int addCart(CartAddDTO cartAddDTO) {
        int result = 0;
        try {
            Cart cart = new Cart();
            BeanUtils.copyProperties(cartAddDTO, cart);
            result = cartMapper.insert(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public int removeCart(List<Long> ids) {
        int result = 0;
        try {
            result = cartMapper.deleteByIds(ids);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public PageResult<CartPageVO> getCartPage(CartPageDTO cartPageDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<CartPageVO> page = cartMapper.getCartPage(
                new Page<>(cartPageDTO.getPageNum(), cartPageDTO.getPageSize()),
                cartPageDTO,
                loginUser.getId()
        );
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
