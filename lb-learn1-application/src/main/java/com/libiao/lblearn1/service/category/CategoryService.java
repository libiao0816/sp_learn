package com.libiao.lblearn1.service.category;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.domain.po.Category;
import com.libiao.lblearn1.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper,Category> {

    private final CategoryMapper categoryMapper;

    public List<Category> selectCategories() {
        return categoryMapper.selectList(null);
    }

    public int insertCategory(Category category) {
        int insert = 0;
        try {
            insert = categoryMapper.insert(category);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return insert;
    }

    public int updateCategory(Category category) {
        int update = 0;
        try {
            update = categoryMapper.updateById(category);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return update;
    }

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
