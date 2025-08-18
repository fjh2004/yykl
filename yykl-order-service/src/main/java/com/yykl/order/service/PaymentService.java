package com.yykl.order.service;

public interface PaymentService {
    /**
     * 处理支付宝支付回调
     * @param notifyData XML格式通知数据
     * @return 处理结果响应
     */
    String handleAlipayPayment(String notifyData);

    /**
     * 处理微信支付回调
     * @param notifyData XML格式通知数据
     * @return 处理结果响应
     */
    String handleWechatPayment(String notifyData);
}