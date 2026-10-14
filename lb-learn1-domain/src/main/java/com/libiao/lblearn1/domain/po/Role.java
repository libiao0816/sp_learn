package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_role")
public class Role {
    
    @TableId(type = IdType.AUTO)
    private Long id;

    private String code;

    private String name;
}
