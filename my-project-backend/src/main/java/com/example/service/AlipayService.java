package com.example.service;

import com.example.entity.Order;
import com.example.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;

public interface AlipayService {
    /**
     *  支付接口
     * @param order
     * @return
     */
    RestBean<String> pay(Order order);

    /**
     *  异步通知
     * @param request
     * @return
     */
    RestBean<String> payNotify(HttpServletRequest request);
}
