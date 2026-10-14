package com.libiao.lblearn1.common.result;

import com.libiao.lblearn1.common.enums.ResultCodeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "结果基类")
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    @Schema(description = "状态码")
    private Integer code;

    @Schema(description = "状态描述")
    private String msg;

    @Schema(description = "数据")
    private T data;

    public static <T> Result<String> Success() {
        Result<String> result = new Result<>();
        result.code = ResultCodeEnum.SUCCESS.getCode();
        result.msg = ResultCodeEnum.SUCCESS.getMsg();
        result.data = "";
        return result;
    }

    public static <T> Result<T> Success(T data) {
        Result<T> result = new Result<>();
        result.code = ResultCodeEnum.SUCCESS.getCode();
        result.msg = ResultCodeEnum.SUCCESS.getMsg();
        result.data = data;
        return result;
    }

    public static <T> Result<T> Fail() {
        Result<T> result = new Result<>();
        result.code = ResultCodeEnum.SYSTEM_ERROR.getCode();
        result.msg = ResultCodeEnum.SYSTEM_ERROR.getMsg();
        return result;
    }

    public static <T> Result<T> Fail(String msg) {
        Result<T> result = new Result<>();
        result.code = ResultCodeEnum.SYSTEM_ERROR.getCode();
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> Fail(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }
}
