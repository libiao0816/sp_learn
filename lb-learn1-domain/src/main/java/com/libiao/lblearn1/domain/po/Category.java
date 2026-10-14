package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_category")
public class Category {
    private Long id;

    private String name;

    private Integer sort;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
