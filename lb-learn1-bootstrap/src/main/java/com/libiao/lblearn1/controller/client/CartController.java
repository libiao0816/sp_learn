package com.libiao.lblearn1.controller.client;

import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.cart.CartAddDTO;
import com.libiao.lblearn1.domain.dto.cart.CartPageDTO;
import com.libiao.lblearn1.domain.dto.cart.CartUpdateQuantityDTO;
import com.libiao.lblearn1.domain.vo.cart.CartPageVO;
import com.libiao.lblearn1.service.cart.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("clientCartController")
@RequestMapping("/client/cart")
@Tag(name = "C端购物车controller", description = "C端购物车：增删分页查")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/page")
    @Operation(summary = "购物车分页", description = "查询当前登录用户的购物车分页")
    public Result<PageResult<CartPageVO>> getCartPage(@RequestBody CartPageDTO cartPageDTO) {
        return Result.Success(cartService.getCartPage(cartPageDTO));
    }

    @PostMapping("/add")
    @Operation(summary = "加入购物车", description = "加入购物车")
    public Result<Integer> addCart(@RequestBody CartAddDTO cartAddDTO) {
        return Result.Success(cartService.addCart(cartAddDTO));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除购物车", description = "按 id 批量删除当前登录用户的购物车")
    public Result<Integer> removeCart(@RequestBody List<Long> ids) {
        return Result.Success(cartService.removeCart(ids));
    }

    @PostMapping("/update")
    @Operation(summary = "修改购物车数量", description = "按 id 修改购物车数量")
    public Result<Integer> removeCart(@RequestBody CartUpdateQuantityDTO cartUpdateQuantityDTO) {
        return Result.Success(cartService.updateCartQuantity(cartUpdateQuantityDTO));
    }
}
