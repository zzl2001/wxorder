package com.imooc.sell.exception;

import com.imooc.sell.enums.ResultEnum;

public class SellException extends RuntimeException {

    private Integer code;

    private String msg;

    public SellException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code=resultEnum.getCode();
    }

    public SellException(ResultEnum resultEnum, String msg) {
        super(msg);
        this.code = resultEnum.getCode();
    }
}
