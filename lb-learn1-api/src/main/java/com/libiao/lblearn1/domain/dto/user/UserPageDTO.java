package com.libiao.lblearn1.domain.dto.user;

import com.libiao.lblearn1.domain.dto.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "测试分页参数")
public class UserPageDTO extends PageQuery {
    
}
