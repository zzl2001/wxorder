package com.imooc.sell.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@DynamicUpdate
@Data
public class ProductCategory {

    /** 类目id.*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    /** 类目名字.*/
    private String categoryName;

    /** 类目编号.*/
    private Integer categoryType;

    private Date updateTime;

    private Date createTime;

//    /** 哪个商家里面的类别*/
//    private String sellerId;

    public ProductCategory(){

    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
