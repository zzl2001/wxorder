package com.imooc.sell.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.utils.EnumUtil;
import com.imooc.sell.utils.serializer.Date2LongSerializer;
import lombok.Data;
import lombok.Getter;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    /* 订单id*/
    private String orderId;

    /** 买家名字.*/
    private String buyerName;

    /** 买家电话.*/
    private String buyerPhone;

    /** 买家地址.*/
    private String buyerAddress;

    /** 买家微信.*/
    private String buyerOpenid;

    /** 订单总金额.*/
    private BigDecimal orderAmount;

    /** 订单状态,默认为新下单.*/
    private Integer orderStatus=OrderStatusEnum.NEW.getCode();

    /** 支付状态.*/
    private Integer payStatus=PayStatusEnum.WAIT.getCode();

    /** 创建时间.*/
    @JsonSerialize(using  = Date2LongSerializer.class)  // 将时间变换为秒
    private Date createTime;

    /** 更新时间.*/
    @JsonSerialize(using  = Date2LongSerializer.class)
    private Date updateTime;

    List<OrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum(){
        return EnumUtil.getByCode(payStatus,PayStatusEnum.class);
    }
}
