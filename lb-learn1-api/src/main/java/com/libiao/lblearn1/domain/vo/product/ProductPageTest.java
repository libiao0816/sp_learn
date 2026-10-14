package com.libiao.lblearn1.domain.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "商品分页测试响应")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageTest {

    @Schema(description = "id")
    private String id;

    @Schema(description = "商品id")
    private String productId;
}
