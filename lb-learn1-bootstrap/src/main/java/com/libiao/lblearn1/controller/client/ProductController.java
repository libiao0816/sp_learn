package com.libiao.lblearn1.controller.client;

import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.product.ProductPageDTO;
import com.libiao.lblearn1.domain.po.Product;
import com.libiao.lblearn1.service.product.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("clientProductController")
@RequestMapping("/client/product")
@Tag(name = "C端商品controller", description = "C端商品：分页查")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/page")
    @Operation(summary = "分页查询商品", description = "分页查询商品")
    public Result<PageResult<Product>> selectProductByPage(@RequestBody ProductPageDTO productPageDTO) {
        return Result.Success(productService.selectProductByPage(productPageDTO));
    }
}
