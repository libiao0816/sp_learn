package com.libiao.lblearn1.mapper;

import com.libiao.lblearn1.domain.po.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolePermissionMapper {

    @Select("select * from t_permission tp " +
            "left join t_role_permission trp on tp.id = trp.permission_id " +
            "left join t_sys_user_role tsur on tsur.role_id = trp.role_id " +
            "where tsur.sys_user_id = #{sysUserId};")
    List<Permission> findPermissionsBySysUserId(Long sysUserId);

}
