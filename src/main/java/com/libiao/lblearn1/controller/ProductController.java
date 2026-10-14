package com.libiao.lblearn1.controller;

import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.dto.product.ProductPageDTO;
import com.libiao.lblearn1.domain.po.Product;
import com.libiao.lblearn1.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Tag(name = "商品controller",description = "sp")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/getDetailById")
    @Operation(summary = "根据商品id查询商品详情", description = "根据商品id查询商品详情")
    public Result<Product> getDetailById(@RequestParam Long id) {
        return Result.Success(productService.selectDetailById(id));
    }

    @Operation(summary = "分页查询商品", description = "支持用户名模糊、状态筛选")
    @PostMapping("/page")
    public Result<PageResult<Product>> test(@RequestBody ProductPageDTO productPageDTO) {
        return Result.Success(productService.selectProductByPage(productPageDTO));
    }


}
