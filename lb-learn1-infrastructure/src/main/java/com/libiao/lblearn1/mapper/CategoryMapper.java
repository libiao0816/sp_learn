package com.libiao.lblearn1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.libiao.lblearn1.domain.po.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
