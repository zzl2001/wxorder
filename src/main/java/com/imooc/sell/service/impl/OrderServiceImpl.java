package com.imooc.sell.service.impl;

import com.imooc.sell.DTO.CartDTO;
import com.imooc.sell.DTO.OrderDTO;
import com.imooc.sell.converter.OrderMasterOrderDTOConverter;
import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.repository.OrderDetailRepository;
import com.imooc.sell.repository.OrderMasterRepository;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.ProductService;
import com.imooc.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        //1、查询商品(数量 价格)

        String orderId = KeyUtil.genUniqueKey();  //创建订单的时候已经生成了这个订单
        BigDecimal orderAmount = new BigDecimal(0);

        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()){
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if(productInfo == null){
             throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2、计算总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        //3、写入订单数据库(orderMaster 和 orderDetail)
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMasterRepository.save(orderMaster);

        //4、扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e ->new CartDTO(e.getProductId(),e.getProductQuantity()) )
                .collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);

        if(orderMaster==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyOpenid,pageable);

        List<OrderDTO> orderDTOList = OrderMasterOrderDTOConverter.convert(orderMasterPage.getContent());

        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancle(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);

        //先判断订单的状态 只有指定的状态下 才能取消
        if(orderDTO.getOrderStatus().equals(OrderStatusEnum.FINISHED.getCode())){
            log.error("【取消订单】 订单状态不正常 ,orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //修改状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster UpdateResult = orderMasterRepository.save(orderMaster);//返回更新后的对象
        if(UpdateResult==null){
            log.error("【取消订单】更新失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        //返回库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】订单中无商品详情,orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
        //如果已经支付 则需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS)){
            //TODO
        }
        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.info("【完结订单】订单状态不确定,orderID={},orderstatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster update = orderMasterRepository.save(orderMaster);
        if(update == null){
            log.info("【订单】更新失败，orderMaster={}",orderMaster);
            throw  new  SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        return orderDTO;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO)  {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.info("【订单支付】错误,orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw  new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.info("【订单支付完成】订单支付状态错误，orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster update = orderMasterRepository.save(orderMaster);
        if(update == null) {
            log.info("【订单完成更新失败】，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable){
        Page<OrderMaster> page = orderMasterRepository.findAll(pageable);

        List<OrderDTO> orderDTOList = OrderMasterOrderDTOConverter.convert(page.getContent());

        return new PageImpl<>(orderDTOList,pageable,page.getTotalElements());
    }
}
