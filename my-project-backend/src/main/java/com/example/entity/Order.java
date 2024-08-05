package com.example.entity;

import lombok.Data;

@Data
public class Order {
    // 订单id
    private String orderId;
    // 价格
    private String price;
    // 商品名称
    private String subject;
    // 商品描述
    private String body;
    // 支付场景
    /**
     * FAST_INSTANT_TRADE_PAY（即时到账）：适用于即时交易场景，买家付款后，卖家立即收到款项。
     * QUICK_MSECURITY_PAY（手机网页支付）：适用于手机网页支付场景。
     * FACE_TO_FACE_PAYMENT（当面付）：适用于线下面对面付款场景，比如扫码支付。
     * APP支付（APP支付场景）：适用于在APP内的支付场景。
     * WAP支付（手机网站支付场景）：适用于手机网站支付场景。
     * PRE_AUTH（预授权）：适用于预先授权场景，买家授权预先冻结资金，商家在完成业务后调用支付宝解冻资金
     */
    private String code;
}
