package com.example.controller;

import com.alipay.api.AlipayApiException;
import com.example.entity.Order;
import com.example.entity.RestBean;
import com.example.service.AlipayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alipay")
public class AlipayController {
    @Autowired
    private AlipayService alipayService;

    /**
     *  支付接口
     * @param order 订单
     * @return
     */
    @PostMapping("/pay")
    public RestBean<String> pay(@RequestBody Order order){
        // 使用时间当订单号
        order.setOrderId(String.valueOf(System.currentTimeMillis()));
        return alipayService.pay(order);
    }

    /**
     *  订单状态通知 异步通知
     * @param request
     * @return
     * @throws AlipayApiException
     */
    @PostMapping("/notify")
    public RestBean<String> payNotify(HttpServletRequest request) throws AlipayApiException {
        return alipayService.payNotify(request);
    }
}

