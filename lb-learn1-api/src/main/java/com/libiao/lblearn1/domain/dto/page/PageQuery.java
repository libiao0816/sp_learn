package com.libiao.lblearn1.domain.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "分页请求基类")
public class PageQuery {

    @NotNull
    @Schema(description = "页码")
    private int pageNum;

    @NotNull
    @Schema(description = "页数")
    private int pageSize;
}
