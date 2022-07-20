package com.hmall.order.common;

import com.hmall.order.config.DelayRabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class DelaySender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDelay(Long orderId) {
        log.info("【订单生成时间】" + new Date().toString() +"【1分钟后检查订单是否已经支付】" + orderId );
        //1.准备消息
        String id = String.valueOf(orderId);
        Message message = MessageBuilder
                .withBody(id.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setExpiration("1800000")
                .build();
        //2.发送消息
        rabbitTemplate.convertAndSend("order.delay.exchange","order_delay",message);
        //3.记录日志
        log.info("消息已经发送成功！");
    }
}
