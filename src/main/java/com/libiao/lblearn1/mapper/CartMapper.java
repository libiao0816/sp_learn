package com.libiao.lblearn1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libiao.lblearn1.domain.dto.cart.CartPageDTO;
import com.libiao.lblearn1.domain.po.Cart;
import com.libiao.lblearn1.domain.vo.cart.CartPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    @Select("select c.id, c.user_id, c.product_id, p.name as product_name, c.quantity, p.price, p.stock as stock " +
            "from t_cart c left join t_product p on c.product_id = p.id " +
            "where c.user_id = #{userId}")
    Page<CartPageVO> getCartPage(Page<CartPageVO> page, CartPageDTO cartPageDTO,Long userId);
}
