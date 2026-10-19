package com.libiao.lblearn1.domain.dto.product;

import com.libiao.lblearn1.domain.dto.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "测试分页参数")
public class ProductPageDTO extends PageQuery {

    @Schema(description = "分类id2")
    private String categoryId;

    @Schema(description = "商品名称2")
    private String productName;
}
