package com.libiao.lblearn1.service.cart;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.cart.CartAddDTO;
import com.libiao.lblearn1.domain.dto.cart.CartPageDTO;
import com.libiao.lblearn1.domain.po.Cart;
import com.libiao.lblearn1.domain.vo.cart.CartPageVO;
import com.libiao.lblearn1.mapper.CartMapper;
import com.libiao.lblearn1.security.sysUser.LoginSysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService extends ServiceImpl<CartMapper, Cart> {

    private final CartMapper cartMapper;

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

    public int removeCart(List<Long> ids) {
        int result = 0;
        try {
            result = cartMapper.deleteByIds(ids);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    public PageResult<CartPageVO> getCartPage(CartPageDTO cartPageDTO) {
        LoginSysUser loginSysUser = (LoginSysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<CartPageVO> page = cartMapper.getCartPage(
                new Page<>(cartPageDTO.getPageNum(), cartPageDTO.getPageSize()),
                cartPageDTO,
                loginSysUser.getId()
        );
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
