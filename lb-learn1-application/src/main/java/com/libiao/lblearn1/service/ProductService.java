package com.libiao.lblearn1.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.product.ProductPageDTO;
import com.libiao.lblearn1.domain.po.Product;

public interface ProductService extends IService<Product> {

    Product selectDetailById(Long id);

    PageResult<Product> selectProductByPage(ProductPageDTO productPageDTO);
}
