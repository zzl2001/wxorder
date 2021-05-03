package com.imooc.sell.service;

import com.imooc.sell.DTO.CartDTO;
import com.imooc.sell.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询上架的商品列表
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     *要选第二个 pageable domain
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);
    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);
}
