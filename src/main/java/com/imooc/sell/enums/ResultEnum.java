package com.imooc.sell.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    PAROM_ERROR(0,"参数不正确"),

    PRODUCT_NOT_EXIST(10,"商品不存在"),

    PRODUCT_STOCK_ERROR(20,"商品数量不够"),

    ORDER_NOT_EXIST(30,"订单不存在"),

    ORDERDETAIL_NOT_EXIST(40,"订单详情不存在"),

    ORDER_UPDATE_FAILED(50,"订单更新失败"),

    ORDER_DETAIL_EMPTY(60,"订单详情为空"),

    ORDER_STATUS_ERROR(70,"订单状态错误"),

    ORDER_PAY_STATUS_ERROR(80,"订单支付状态错误"),

    CART_EMPTY(90,"购物车为空"),

    ORDER_OWNER_ERROR(100,"该订单不属于当前用户"),

    WX_MP_ERROR(110,"微信公众账号方面错误"),

    SUCCESS(0,"成功"),

    ORDER_CANCEL_SUCCESS(120,"订单取消成功"),

    ORDER_FINISH_SUCCESS(130,"订单完结成功"),

    PRODUCT_STATUS_ERROR(140,"订单状态不正确"),

    LOGIN_FAIL(150,"登陆信息不正确"),

    LOGOUT_SUCCESS(160,"登出成功"),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
