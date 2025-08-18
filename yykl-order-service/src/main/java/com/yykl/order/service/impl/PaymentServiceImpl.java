package com.yykl.order.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.yykl.order.config.PaymentConfig;
import com.yykl.order.enums.OrderEvent;
import com.yykl.order.feign.ProductFeignClient;
import com.yykl.order.mapper.OrderMapper;
import com.yykl.order.model.dto.ProductStockDTO;
import com.yykl.order.model.entity.Order;
import com.yykl.order.service.PaymentService;
import com.yykl.order.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.baomidou.mybatisplus.extension.toolkit.Db.getById;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private OrderMapper baseMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PaymentConfig paymentConfig;

    @Override
    public String handleAlipayPayment(String notifyData) {
        Map<String, String> params = null;
        try {
            params = parseAlipayResponse(notifyData);
            if (!params.containsKey("sign") || !params.containsKey("out_trade_no")) {
                log.warn("支付宝回调参数异常");
                log.warn("支付宝验签失败，原始参数：{}", params);
                return "failure";
            }

            // 1. 支付宝签名验证
            if (!verifyAlipaySignature(params)) {
                log.warn("支付宝验签失败，原始参数：{}", params);
                return "failure";
            }

            // 2. 幂等性检查
            String tradeNo = params.get("trade_no");
            if (redisTemplate.opsForValue().get(tradeNo) != null) {
                return "success";
            }

            // 3. 更新订单状态
            String orderNo = params.get("out_trade_no");
            validateStateTransition(orderNo, OrderEvent.PAY);
            boolean updateSuccess = updateOrderStatus(orderNo, 2);

            if (updateSuccess) {
                redisTemplate.opsForValue().set(tradeNo, "processed", 24, TimeUnit.HOURS);
                return "success";
            }
            log.warn("支付宝验签失败，原始参数：{}", params);
            return "failure";
        } catch (Exception e) {
            log.error("支付宝回调处理异常", e);
            log.warn("支付宝验签失败，原始参数：{}", params);
            return "failure";
        }
    }

    @Override
    public String handleWechatPayment(String notifyData) {
        try {
            Map<String, String> params = XmlUtils.xmlToMap(notifyData);
            
            // 1. 验证签名
            if (!verifyWechatSignature(params)) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }

            // 2. 幂等性检查
            String transactionId = params.get("transaction_id");
            if (redisTemplate.opsForValue().get(transactionId) != null) {
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
            }

            // 3. 更新订单状态
            String orderNo = params.get("out_trade_no");
            validateStateTransition(orderNo, OrderEvent.PAY);
            boolean updateSuccess = baseMapper.updateOrderStatus(orderNo, 2) > 0;

            if (updateSuccess) {
                // 4. 记录支付流水（24小时过期）
                redisTemplate.opsForValue().set(transactionId, "processed", 24, TimeUnit.HOURS);
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
            }
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error("微信支付回调处理异常", e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }

    private boolean verifyWechatSignature(Map<String, String> params) {
        try {
            String sign = params.get("sign");
            String apiKey = paymentConfig.getWechatApiKey();
            
            // 生成待签名字符串
            String sortedParams = params.entrySet().stream()
                .filter(e -> !"sign".equals(e.getKey()) && StringUtils.isNotEmpty(e.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

            String localSign = DigestUtils.md5Hex(sortedParams + "&key=" + apiKey).toUpperCase();
            return sign.equals(localSign);
        } catch (Exception e) {
            log.error("微信签名验证失败", e);
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean updateOrderStatus(String orderNo, int status) {
        try {
            // 1. 更新订单状态
            boolean updateSuccess = baseMapper.updateOrderStatus(orderNo, status) > 0;
            
            // 2. 扣减真实库存（分布式事务）
            Order order = Db.getById(orderNo, Order.class);
            ProductStockDTO stockDTO = new ProductStockDTO();
            stockDTO.setProductId(order.getProductId());
            stockDTO.setQuantity(order.getQuantity());
            if (!productFeignClient.deductStock(stockDTO)) {
                throw new RuntimeException("库存扣减失败");
            }
            
            return updateSuccess;
        } catch (Exception e) {
            log.error("订单状态更新失败", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    private Map<String, String> parseAlipayResponse(String notifyData) {
        return Arrays.stream(notifyData.split("&"))
            .map(param -> param.split("=", 2))
            .collect(Collectors.toMap(
                arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8),
                arr -> URLDecoder.decode(arr.length > 1 ? arr[1] : "", StandardCharsets.UTF_8)
            ));
    }

    private boolean verifyAlipaySignature(Map<String, String> params) {
        try {
            String sign = params.get("sign");
            String content = params.entrySet().stream()
                .filter(e -> !"sign".equals(e.getKey()) && !e.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(new ByteArrayInputStream(
                paymentConfig.getAlipay().getAlipayPublicKey().getBytes(StandardCharsets.UTF_8)));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(cert.getPublicKey());
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            log.error("支付宝签名验证异常", e);
            return false;
        }
    }

    private void validateStateTransition(String orderNo, OrderEvent event) {
        if (!stateMachineService.sendEvent(orderNo, event)) {
            throw new IllegalStateException("支付状态转换异常");
        }
    }

    @Autowired
    private OrderStateMachineService stateMachineService;
}