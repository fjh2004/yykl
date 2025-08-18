package com.yykl.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付渠道配置
 * 微信支付配置示例：
 * wechat:
 *   appId: wx1234567890
 *   mchId: 1900000109
 *   apiKey: your_wechat_api_key_32位
 * 
 * 支付宝配置示例：
 * alipay:
 *   appId: 2021003123456789
 *   merchantPrivateKey: 商户私钥
 *   alipayPublicKey: 支付宝公钥
 */
@Data
@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {
    private WechatConfig wechat;
    private AlipayConfig alipay;

    @Data
    public static class WechatConfig {
        private String appId;
        private String mchId;
        private String apiKey;
        private String certPath;
    }

    @Data
    public static class AlipayConfig {
        private String appId;
        private String merchantPrivateKey;
        private String alipayPublicKey;
        private String notifyUrl;
        private String returnUrl;
    }

    public String getWechatApiKey() {
        return wechat != null ? wechat.getApiKey() : null;
    }
}