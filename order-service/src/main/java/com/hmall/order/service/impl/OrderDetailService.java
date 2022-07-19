package com.hmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.order.mapper.OrderDetailMapper;
import com.hmall.order.pojo.OrderDetail;
import com.hmall.order.service.IOrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {
}
