package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝支付配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String gateway;
}
