package com.libiao.lblearn1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.libiao.lblearn1.domain.po.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("select tr.code from t_role tr left join t_user_role tur " +
            "on tr.id = tur.role_id " +
            "where tur.user_id = #{userId};")
    List<String> findRoleCodesByUserId(Long userId);
}
