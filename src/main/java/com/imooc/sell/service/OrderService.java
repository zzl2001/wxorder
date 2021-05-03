package com.imooc.sell.service;

import com.imooc.sell.DTO.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    /** 创建订单.*/
    OrderDTO create(OrderDTO orderDTO);

    /** 查询单个订单.*/
    OrderDTO findOne(String orderId);

    /** 查询某个人订单列表.*/
    Page<OrderDTO> findList(String buyOpenid, Pageable pageable);

    /** 取消订单.*/
    OrderDTO cancle(OrderDTO orderDTO);

    /** 完结订单.*/
    OrderDTO finish(OrderDTO orderDTO);

    /** 支付订单.*/
    OrderDTO paid(OrderDTO orderDTO);

    /** 查询所有订单列表 */
    Page<OrderDTO> findList(Pageable pageable);
}
