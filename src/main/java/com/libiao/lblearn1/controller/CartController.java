package com.libiao.lblearn1.controller;

import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.cart.CartAddDTO;
import com.libiao.lblearn1.domain.dto.cart.CartPageDTO;
import com.libiao.lblearn1.domain.vo.cart.CartPageVO;
import com.libiao.lblearn1.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cart")
@Tag(name = "购物车controller",description = "购物车")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/page")
    @Operation(summary = "获取购物车分页", description = "获取购物车分页")
    public Result<PageResult<CartPageVO>> getCartPage(CartPageDTO cartPageDTO) {
        return Result.Success(cartService.getCartPage(cartPageDTO));
    }
    
    @PostMapping("/add")
    @Operation(summary = "添加购物车", description = "添加购物车")
    public Result<Integer> addCart(@RequestBody CartAddDTO cartAddDTO) {
        return Result.Success(cartService.addCart(cartAddDTO));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除购物车", description = "删除购物车")
    public Result<Integer> removeCart(@RequestBody List<Long> ids) {
        return Result.Success(cartService.removeCart(ids));
    }
}
