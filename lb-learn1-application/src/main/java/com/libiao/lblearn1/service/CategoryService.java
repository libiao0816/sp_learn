package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.domain.po.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> selectCategories();

    int insertCategory(Category category);

    int updateCategory(Category category);

    int deleteCategory(List<Long> idList);
}
