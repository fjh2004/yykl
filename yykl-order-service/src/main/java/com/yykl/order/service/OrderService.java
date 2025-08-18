package com.yykl.order.service;

import com.yykl.order.model.dto.OrderCreateDTO;

public interface OrderService {
    /**
     * 创建订单（分布式事务）
     * @param orderDTO 订单创建传输对象
     * @return 订单编号
     */
    String createOrder(OrderCreateDTO orderDTO);

    /**
     * 取消订单（状态机驱动）
     *
     * @param orderNo 订单编号
     * @return
     */
    boolean cancelOrder(String orderNo);
}