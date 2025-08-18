package com.yykl.order.service.impl;

import com.yykl.order.enums.OrderEvent;
import com.yykl.order.enums.OrderState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderStateMachineService {
    private final StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;

    public OrderStateMachineService(StateMachineFactory<OrderState, OrderEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public boolean sendEvent(String orderId, OrderEvent event) {
        StateMachine<OrderState, OrderEvent> stateMachine = stateMachineFactory.getStateMachine(orderId);
        return stateMachine.sendEvent(event);
    }

    public OrderState getCurrentState(String orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = stateMachineFactory.getStateMachine(orderId);
        return stateMachine.getState().getId();
    }
}