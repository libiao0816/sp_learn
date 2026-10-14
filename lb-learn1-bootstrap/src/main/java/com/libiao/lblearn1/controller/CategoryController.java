package com.libiao.lblearn1.controller;

import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.po.Category;
import com.libiao.lblearn1.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    
    @Operation(summary = "获取所有分类", description = "获取所有分类")
    @GetMapping("/list")
    public Result<List<Category>> selectCategories() {
        return Result.Success(categoryService.selectCategories());
    }

}
