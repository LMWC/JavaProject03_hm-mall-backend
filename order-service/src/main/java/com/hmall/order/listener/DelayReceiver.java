package com.hmall.order.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hmall.common.client.ItemClient;
import com.hmall.order.mapper.OrderDetailMapper;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDetail;
import com.hmall.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class DelayReceiver {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ItemClient itemClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "order.queue", durable = "true"),
            exchange = @Exchange(name = "order.exchange"),
            key = "order"
    ))
    public void listenDelayedlQueue(String msg){
        log.info("接收到的延迟消息：{}", msg);
        long orderId = Long.parseLong(msg);
        Order order = (Order) orderService.getById(orderId);
        log.info("【orderDelayQueue 监听的消息】 - 【消费时间】 - [{}]- 【订单内容】 - [{}]", new Date(), order.toString());
        //- 当监听者收到消息后，一定是下单30分钟后。根据订单id查询订单信息，判断status是否已经支付：
        //- 如果未支付：肯定是超时未支付订单，将其**status修改为5**，取消订单，**恢复扣减的库存**
        //- 如果是已支付，则丢弃消息不管
        Integer orderStatus = order.getStatus();
        if (orderStatus == 1) {
            LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Order::getStatus, 5)
                    .set(Order::getUpdateTime, new Date())
                    .eq(Order::getId, orderId);
            orderService.update(wrapper);
            //根据order订单查询订单明细查到商品明细进行远程调用
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(OrderDetail::getOrderId, orderId)
                    .select(OrderDetail::getItemId, OrderDetail::getNum);
            OrderDetail orderDetail = orderDetailMapper.selectOne(queryWrapper);
            //库存回滚 调用item-service，根据商品id、商品数量恢复库存
            itemClient.rollBackItem(orderDetail.getItemId(),orderDetail.getNum());
        }
    }
}
