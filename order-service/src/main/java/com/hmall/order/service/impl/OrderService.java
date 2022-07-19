package com.hmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.client.ItemClient;
import com.hmall.common.client.UserClient;
import com.hmall.common.dto.Address;
import com.hmall.common.dto.Item;
import com.hmall.order.dto.RequestParams;
import com.hmall.order.mapper.OrderMapper;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDetail;
import com.hmall.order.pojo.OrderLogistics;
import com.hmall.order.service.IOrderDetailService;
import com.hmall.order.service.IOrderLogisticService;
import com.hmall.order.service.IOrderService;
import com.hmall.order.utils.UserHolder;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private IOrderLogisticService orderLogisticService;

    @Autowired
    private ItemClient itemClient;
    @Autowired
    private UserClient userClient;

    //设计多数据库操作，使用seata的全局事务处理
    @GlobalTransactional
    @Override
    public Long submitOrder(RequestParams params) {
        //查询当前订单包含的商品
        Item item = itemClient.queryItemById(params.getItemId());
        Address address = userClient.findAddressById(params.getAddressId());

        //1.订单库--订单表--增加
        Order order = new Order();
        order.setTotalFee(params.getNum()*item.getPrice());
        order.setPaymentType(params.getPaymentType());
        order.setUserId(UserHolder.getUser());
        order.setStatus(1);
        order.setCreateTime(getCurrentTime());
        order.setUpdateTime(getCurrentTime());
        this.save(order);

        //2.订单库--订单详情表--增加
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getId());
        orderDetail.setItemId(item.getId());
        orderDetail.setNum(params.getNum());
        orderDetail.setTitle(item.getName());
        orderDetail.setPrice(item.getPrice());
        orderDetail.setSpec(item.getSpec());
        orderDetail.setImage(item.getImage());
        orderDetail.setCreateTime(getCurrentTime());
        orderDetail.setUpdateTime(getCurrentTime());
        orderDetailService.save(orderDetail);
        //3.订单库--订单物流表--增加
        OrderLogistics orderLogistics = new OrderLogistics();
        orderLogistics.setOrderId(order.getId());
        String string = UUID.randomUUID().toString();
        orderLogistics.setLogisticsNumber(string.replace("-","").substring(0,18));
        BeanUtils.copyProperties(address,orderLogistics,"id","userId","isDefault");

        orderLogistics.setCreateTime(getCurrentTime());
        orderLogistics.setUpdateTime(getCurrentTime());
        orderLogisticService.save(orderLogistics);
        //4.商品库--更新库存
        item.setStock((item.getStock()-params.getNum()));
        try {
            itemClient.update(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order.getId();
    }

    //获取系统当前时间
    private Date getCurrentTime() {
        Date currentTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date());
            currentTime = sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTime;
    }
}
