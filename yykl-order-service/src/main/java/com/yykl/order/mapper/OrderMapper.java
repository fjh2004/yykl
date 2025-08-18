package com.yykl.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yykl.order.model.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Update("UPDATE product SET stock = stock - #{quantity} WHERE id = #{productId} AND stock >= #{quantity}")
    int deductStock(@Param("productId") String productId, @Param("quantity") Integer quantity);

    @Update("UPDATE product SET stock = stock + #{quantity} WHERE id = #{productId}")
    int rollbackStock(@Param("productId") String productId, @Param("quantity") Integer quantity);

    @Update("UPDATE `t_order` SET status = #{status} WHERE order_no = #{orderNo}")
    int updateOrderStatus(@Param("orderNo") String orderNo, @Param("status") Integer status);
}