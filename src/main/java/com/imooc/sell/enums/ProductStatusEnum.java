package com.imooc.sell.enums;


import lombok.Getter;

@Getter
public enum ProductStatusEnum implements CodeEnum{

    UP(0,"在架"),
    DOWN(1,"下架")
    ;
    private Integer code;

    private String Message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.Message = message;
    }
}
