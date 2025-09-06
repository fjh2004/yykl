package com.yykl.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yykl.order.model.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    // 已有的库存和状态更新方法
    @Update("UPDATE product SET stock = stock - #{quantity} WHERE id = #{productId} AND stock >= #{quantity}")
    int deductStock(@Param("productId") String productId, @Param("quantity") Integer quantity);

    @Update("UPDATE product SET stock = stock + #{quantity} WHERE id = #{productId}")
    int rollbackStock(@Param("productId") String productId, @Param("quantity") Integer quantity);

    @Update("UPDATE `t_order` SET order_state = #{status}, update_time = CURRENT_TIMESTAMP WHERE order_no = #{orderNo}")
    int updateOrderStatus(@Param("orderNo") String orderNo, @Param("status") Integer status);

    // 新增：个人订单分页查询
    IPage<Order> selectPersonalOrders(Page<Order> page,
                                      @Param("userId") Long userId,
                                      @Param("orderState") Integer orderState);

    // 新增：所有订单分页查询（商家用）
    IPage<Order> selectAllOrders(Page<Order> page, @Param("orderState") Integer orderState);
}