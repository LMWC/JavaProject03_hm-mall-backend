package com.hmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.order.dto.RequestParams;
import com.hmall.order.pojo.Order;

public interface IOrderService extends IService<Order> {
    Long submitOrder(RequestParams params);
}
