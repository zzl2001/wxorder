package com.imooc.sell.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imooc.sell.enums.ProductStatusEnum;
import com.imooc.sell.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Id;
import javax.persistence.Entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@DynamicUpdate
public class ProductInfo {

    @Id
    private String productId;

    /** 名称.*/
    private String productName;

    /** 单价.*/
    private BigDecimal productPrice;

    /** 库存.*/
    private Integer productStock;

    /** 描述.*/
    private String productDescription;

    /** 小图.*/
    private String productIcon;

    /** 状态 0为正常.*/
    private Integer productStatus=ProductStatusEnum.UP.getCode();

    /** 类目编号.*/
    private Integer categoryType;

//    /** 对应的是谁卖的*/
//    private String sellerId;

    private Date createTime;

    private Date updateTime;

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum(){
        return EnumUtil.getByCode(productStatus,ProductStatusEnum.class);
    }
}
