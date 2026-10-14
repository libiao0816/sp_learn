package com.libiao.lblearn1.common.exception;

import com.libiao.lblearn1.common.enums.ResultCodeEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCodeEnum.SYSTEM_ERROR.getCode();
    }

    public BusinessException(ResultCodeEnum result) {
        super(result.getMsg());
        this.code = result.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
