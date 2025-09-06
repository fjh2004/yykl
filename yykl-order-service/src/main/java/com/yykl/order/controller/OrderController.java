package com.yykl.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yykl.order.model.dto.OrderCreateDTO;
import com.yykl.order.model.dto.OrderQueryDTO;
import com.yykl.order.model.entity.Order;
import com.yykl.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders") // 统一订单接口前缀，与查询接口保持一致
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 创建订单接口（对应前端“购买”按钮点击事件）
    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderCreateDTO orderDTO) {
        // 调用服务层创建订单，返回订单号
        String orderNo = orderService.createOrder(orderDTO);
        // 前端可使用该订单号跳转至支付页面或查询订单状态
        return ResponseEntity.ok(orderNo);
    }

    // 已有的个人查询订单接口
    @GetMapping("/personal")
    public IPage<Order> getPersonalOrders(@Valid OrderQueryDTO queryDTO) {
        return orderService.queryPersonalOrders(queryDTO);
    }

    // 已有的商家查询订单接口
    @GetMapping("/merchant")
    public IPage<Order> getMerchantOrders(@Valid OrderQueryDTO queryDTO) {
        return orderService.queryAllOrders(queryDTO);
    }
}