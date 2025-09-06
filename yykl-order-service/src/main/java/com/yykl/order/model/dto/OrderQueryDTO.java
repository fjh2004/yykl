// OrderQueryDTO.java
package com.yykl.order.model.dto;

import com.yykl.order.enums.OrderState;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class OrderQueryDTO {
    /**
     * 用户ID（个人查询时必传）
     */
    private Long userId;

    /**
     * 订单状态（可选）
     */
    private OrderState orderState;

    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    private Integer pageSize = 10;
}
