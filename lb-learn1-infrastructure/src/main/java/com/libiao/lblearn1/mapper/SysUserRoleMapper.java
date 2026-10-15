package com.libiao.lblearn1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.libiao.lblearn1.domain.po.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("select tr.code from t_role tr left join t_sys_user_role tsur " +
            "on tr.id = tsur.role_id " +
            "where tsur.sys_user_id = #{sysUserId};")
    List<String> findRoleCodesBySysUserId(Long sysUserId);
}
