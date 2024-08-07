package com.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.entity.Order;
import com.example.entity.RestBean;
import com.example.service.AlipayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {
    @Value("${alipay.alipayPublicKey}")
    private String publicKey;
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;
    @Value("${alipay.returnUrl}")
    private String returnUrl;

    @Autowired
    private AlipayClient alipayClient;
    @Override
    public RestBean<String> pay(Order order){
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(returnUrl);
        // 异步通知的地址
        request.setNotifyUrl(notifyUrl);
        Map<String,String> map = new HashMap<>();
        map.put("out_trade_no",order.getOrderId());
        map.put("total_amount",order.getPrice());
        map.put("subject",order.getSubject());
        map.put("body",order.getBody());
        map.put("product_code","FAST_INSTANT_TRADE_PAY");

        // 设置业务参数
        request.setBizContent(JSONObject.toJSONString(map));

        // 发起支付请求
        // 发起支付请求
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        // 获取响应结果
        if (response.isSuccess()) {
            System.out.println("调用成功");
            System.out.println("支付宝支付链接：" + response.getBody());
            return RestBean.success(response.getBody());
        } else {
            System.out.println("调用失败");
            System.out.println("错误信息：" + response.getMsg());
            return RestBean.failure(40010,"调用失败");
        }
    }

    @Override
    public RestBean<String> payNotify(HttpServletRequest request) {

        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            StringBuilder valueStr = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                valueStr.append((i == values.length - 1) ? values[i] : values[i] + ",");
            }
            params.put(name, valueStr.toString());
        }

        // 调用SDK验证签名
        boolean verifyResult = false;
        try {
            verifyResult = AlipaySignature.rsaCheckV1(params, publicKey, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

        if (verifyResult) {
            // 验证成功，处理业务逻辑，更新订单状态等
            // 注意：请防止重复处理，可以通过记录支付状态或者订单号来判断是否已经处理过
            System.out.println("支付宝异步通知验证成功");
            // 返回给支付宝成功处理的响应
            return RestBean.success();
        } else {
            // 验证失败，不处理业务逻辑
            System.out.println("支付宝异步通知验证失败");
            // 返回给支付宝失败处理的响应
            return RestBean.failure(40010,"支付失败");
        }
    }
}
