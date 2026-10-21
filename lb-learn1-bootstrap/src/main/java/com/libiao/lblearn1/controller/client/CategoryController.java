package com.libiao.lblearn1.controller.client;

import com.libiao.lblearn1.common.result.Result;
import com.libiao.lblearn1.domain.po.Category;
import com.libiao.lblearn1.service.category.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("clientCategoryController")
@RequestMapping("/client/category")
@Tag(name = "C端分类controller", description = "C端分类：查询")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "获取所有分类", description = "获取所有分类")
    public Result<List<Category>> selectCategories() {
        return Result.Success(categoryService.selectCategories());
    }
}
