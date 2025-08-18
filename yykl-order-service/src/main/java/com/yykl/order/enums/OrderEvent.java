package com.yykl.order.enums;

/**
 * 订单状态机事件枚举
 */
public enum OrderEvent {
    /** 支付事件（CREATED -> PAID） */
    PAY(OrderState.CREATED, OrderState.PAID),
    /** 取消事件（CREATED -> CANCELED） */
    CANCEL(OrderState.CREATED, OrderState.CANCELED),
    /** 完成支付确认（PAID -> COMPLETED） */
    CONFIRM(OrderState.PAID, OrderState.COMPLETED);

    private final OrderState sourceState;
    private final OrderState targetState;

    OrderEvent(OrderState sourceState, OrderState targetState) {
        this.sourceState = sourceState;
        this.targetState = targetState;
    }

    public OrderState getSourceState() {
        return sourceState;
    }

    public OrderState getTargetState() {
        return targetState;
    }
}