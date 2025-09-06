package com.yykl.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yykl.order.model.dto.OrderCreateDTO;
import com.yykl.order.model.dto.OrderQueryDTO;
import com.yykl.order.model.entity.Order;

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

    /**
     * 个人查询订单
     * @param queryDTO 查询条件（包含用户ID）
     * @return 分页订单列表
     */
    IPage<Order> queryPersonalOrders(OrderQueryDTO queryDTO);

    /**
     * 商家查询所有订单
     * @param queryDTO 查询条件
     * @return 分页订单列表
     */
    IPage<Order> queryAllOrders(OrderQueryDTO queryDTO);
}