package com.hmall.search.lister;

import com.hmall.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ItemLister {

    @Autowired
    private SearchService searchService;

    //监听插入
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue("item.insert.queue"),
                    exchange = @Exchange(value = "item.topic",type = ExchangeTypes.TOPIC),
                    key = "item.insert"
            )
    )
    public void listerItemInsert(Long id){
        boolean insert = searchService.insertById(id);
        log.info("es中同步数据---(添加):{}",insert?"成功":"失败");
    }

    //监听删除
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue("item.delete.queue"),
                    exchange = @Exchange(value = "item.topic",type = ExchangeTypes.TOPIC),
                    key = "item.delete"
            )
    )
    public void listerItemDelete(Long id){
        boolean delete = searchService.deleteById(id);
        log.info("es中同步数据---(删除):{}",delete?"成功":"失败");
    }
}
