package com.libiao.lblearn1.domain.dto.cart;

import com.libiao.lblearn1.domain.dto.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "购物车分页查询参数")
public class CartPageDTO extends PageQuery {

}
