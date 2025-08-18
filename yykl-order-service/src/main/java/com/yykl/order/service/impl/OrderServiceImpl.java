package com.yykl.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yykl.order.enums.OrderEvent;
import com.yykl.order.enums.OrderState;
import com.yykl.order.mapper.OrderMapper;
import com.yykl.order.model.dto.OrderCreateDTO;
import com.yykl.order.model.entity.Order;
import com.yykl.order.service.OrderService;
import com.yykl.order.util.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单服务实现
 * 核心流程：
 * 1. 库存预校验（Redis原子操作）
 * 2. 生成订单号（雪花算法）
 * 3. 创建订单主表
 * 4. 创建订单明细
 * 5. 扣减真实库存（MySQL行锁）
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private OrderStateMachineService stateMachineService;

    @Override
    @Transactional
    public String createOrder(OrderCreateDTO orderDTO) {
        // 分布式锁保证库存操作原子性
        String lockKey = "stock_lock:" + orderDTO.getProductId();
        String lockValue = redisLockUtil.tryLock(lockKey, 3000);

        try {
            // 校验库存（Redis预扣减）
            String stockKey = "product_stock:" + orderDTO.getProductId();
            Long stock = redisTemplate.opsForValue().decrement(stockKey, orderDTO.getQuantity());
            
            if (stock != null && stock >= 0) {
                // 真实库存扣减（MySQL事务）
                baseMapper.deductStock(orderDTO.getProductId(), orderDTO.getQuantity());
                
                // 生成订单（雪花算法）
                Order order = new Order();
                order.setOrderNo(generateOrderNo());
                order.setOrderState(OrderState.CREATED.ordinal());
                order.setUserId(orderDTO.getUserId());
                order.setTotalAmount(orderDTO.getTotalFee());
                this.save(order);
                
                return order.getOrderNo();
            }
            throw new RuntimeException("库存不足");
        } finally {
            redisLockUtil.unlock(lockKey, lockValue);
        }
    }

    private String generateOrderNo() {
        // 雪花算法实现（已省略）
        return "DD" + System.currentTimeMillis();
    }

    private void validateStateTransition(String orderId, OrderEvent event) {
        if (!stateMachineService.sendEvent(orderId, event)) {
            throw new IllegalStateException("订单状态转换异常");
        }
    }

    @Override
    @Transactional
    public boolean cancelOrder(String orderNo) {
        String lockKey = "order_cancel_lock:" + orderNo;
        String lockValue = redisLockUtil.tryLock(lockKey, 3000);
        try {
            Order order = baseMapper.selectById(orderNo);
            if (order == null) {
                throw new RuntimeException("订单不存在");
            }
            
            // 状态机校验
            validateStateTransition(orderNo, OrderEvent.CANCEL);
            
            // 执行取消操作
            boolean updateSuccess = baseMapper.updateOrderStatus(orderNo, OrderState.CANCELED.ordinal()) > 0;
            
            // 库存回滚（需要实现）
            rollbackStock(order.getProductId(), order.getQuantity());
            
            return updateSuccess;
        } finally {
            redisLockUtil.unlock(lockKey, lockValue);
        }
    }

    private void rollbackStock(String productId, Integer quantity) {
        // 实现库存回滚逻辑
        String stockKey = "product_stock:" + productId;
        redisTemplate.opsForValue().increment(stockKey, quantity);
        baseMapper.rollbackStock(productId, quantity);
    }
}