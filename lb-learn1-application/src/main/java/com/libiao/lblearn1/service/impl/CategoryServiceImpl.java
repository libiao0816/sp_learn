package com.libiao.lblearn1.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.Category;
import com.libiao.lblearn1.mapper.CategoryMapper;
import com.libiao.lblearn1.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> selectCategories() {
        return categoryMapper.selectList(null);
    }

    @Override
    public int insertCategory(Category category) {
        int insert = 0;
        try {
            insert = categoryMapper.insert(category);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return insert;
    }

    @Override
    public int updateCategory(Category category) {
        int update = 0;
        try {
            update = categoryMapper.updateById(category);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return update;
    }

    @Override
    public int deleteCategory(List<Long> idList) {
        int delete = 0;
        try {
            delete = categoryMapper.deleteByIds(idList);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return delete;
    }
}
