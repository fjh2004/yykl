package com.yykl.order.controller;

import com.yykl.order.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentCallbackController {

    private final PaymentService paymentService;

    public PaymentCallbackController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 微信支付回调
    @PostMapping("/wechat/notify")
    public String wechatNotify(@RequestBody String notifyData) {
        return paymentService.handleWechatPayment(notifyData);
    }

    // 支付宝支付回调
    @PostMapping("/alipay/notify")
    public String alipayNotify(@RequestBody String notifyData) {
        return paymentService.handleAlipayPayment(notifyData);
    }
}