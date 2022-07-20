package com.hmall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayRabbitConfig {
    // 延迟队列 TTL 名称
    private static final String ORDER_DELAY_QUEUE = "order.delay.queue";

    // 延时消息就是发送到该交换机的
    public static final String ORDER_DELAY_EXCHANGE = "order.delay.exchange";

    // routing key 名称
    // 具体消息发送在该 routingKey 的
    public static final String ORDER_DELAY_ROUTING_KEY = "order_delay";

    //立即消费的队列名称
    public static final String ORDER_QUEUE_NAME = "order.queue";

    // 立即消费的exchange
    public static final String ORDER_EXCHANGE_NAME = "order.exchange";

    //立即消费 routing key 名称
    public static final String ORDER_ROUTING_KEY = "order";

    //延迟消费交换机
    @Bean
    public DirectExchange orderDelayExchange() {
        // 一共有三种构造方法，可以只传exchange的名字， 第二种，可以传exchange名字，是否支持持久化，是否可以自动删除，
        // 第三种在第二种参数上可以增加Map，Map中可以存放自定义exchange中的参数
        // new DirectExchange(ORDER_DELAY_EXCHANGE,true,false);
        return new DirectExchange(ORDER_DELAY_EXCHANGE);
    }

    //创建一个延时队列
    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder
                .durable(ORDER_DELAY_QUEUE)
                //.ttl(20000)//设置队列的超时时间，20秒
                .deadLetterExchange(ORDER_EXCHANGE_NAME)
                .deadLetterRoutingKey(ORDER_ROUTING_KEY)
                .build();

    }

    // 把延时队列和 订单延迟交换的exchange进行绑定
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(orderDelayQueue()).to(orderDelayExchange()).with(ORDER_DELAY_ROUTING_KEY);
    }

    //立即消费交换机
    @Bean
    public DirectExchange orderTopicExchange() {
        return new DirectExchange(ORDER_EXCHANGE_NAME);
    }

    // 创建一个立即消费队列
    @Bean
    public Queue orderQueue() {
        // 第一个参数为queue的名字，第二个参数为是否支持持久化
        return new Queue(ORDER_QUEUE_NAME, true);
    }

    // 把立即队列和 立即交换的exchange进行绑定
    @Bean
    public Binding orderBinding() {
        // TODO 如果要让延迟队列之间有关联,这里的 routingKey 和 绑定的交换机很关键
        return BindingBuilder.bind(orderQueue()).to(orderTopicExchange()).with(ORDER_ROUTING_KEY);
    }

}
