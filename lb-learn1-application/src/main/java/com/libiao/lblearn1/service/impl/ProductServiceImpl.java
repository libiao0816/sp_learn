package com.libiao.lblearn1.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.libiao.lblearn1.common.enums.ProductCodeEnum;
import com.libiao.lblearn1.common.exception.BusinessException;
import com.libiao.lblearn1.common.result.PageResult;
import com.libiao.lblearn1.domain.dto.product.ProductPageDTO;
import com.libiao.lblearn1.domain.po.Product;
import com.libiao.lblearn1.mapper.ProductMapper;
import com.libiao.lblearn1.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements ProductService {

    @Override
    public PageResult<Product> selectProductByPage(ProductPageDTO productPageDTO) {
        Page<Product> productPage = lambdaQuery()
                .eq(Objects.nonNull(productPageDTO.getCategoryId()), Product::getCategoryId, productPageDTO.getCategoryId())
                .like(Objects.nonNull(productPageDTO.getProductName()), Product::getName, productPageDTO.getProductName())
                .page(new Page<>(productPageDTO.getPageNum(), productPageDTO.getPageSize()));
        return new PageResult<>(productPage.getTotal(), productPage.getRecords());
    }

    @Override
    public Product selectDetailById(Long id) {
        Product productDetail = getById(id);
        if(Objects.isNull(productDetail)){
            throw new BusinessException(ProductCodeEnum.PRODUCT_NOT_FOUND.getCode(), ProductCodeEnum.PRODUCT_NOT_FOUND.getMsg());
        }
        return productDetail;
    }
}
