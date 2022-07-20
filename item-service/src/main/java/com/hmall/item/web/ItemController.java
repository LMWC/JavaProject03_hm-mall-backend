package com.hmall.item.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //根据id查询商品
    @GetMapping("/{id}")
    public Item getById(@PathVariable("id") Long id){
        return itemService.getById(id);
    }

    //分页查询商品(基于mysql)
    @GetMapping("/list")
    public PageDTO<Item> list(@RequestParam int page, @RequestParam int size){
        log.info("当前页:{}",page);
        log.info("页大小:{}",size);
        //LambdaQueryWrapper<Item> wrapper = new LambdaQueryWrapper<>();
        Page<Item> pageInfo = new Page<>(page,size);
        itemService.page(pageInfo);
        return new PageDTO<>(pageInfo.getTotal(),pageInfo.getRecords());
    }

    //新增商品
    @PostMapping
    public void save(@RequestBody Item item){
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        boolean save = itemService.save(item);
        log.info("保存结果:{}",save?"成功":"失败");
    }

    //上架、下架商品
    //@GlobalTransactional
    @Transactional
    @PutMapping("/status/{id}/{status}")
    public void status(@PathVariable("id") Long id,@PathVariable("status") int status){
        Item item = itemService.getById(id);
        item.setStatus(status);
        item.setUpdateTime(this.getCurrentTime());
        boolean update = itemService.updateById(item);
        log.info("上下架结果:{}",update?"成功":"失败");
        if (status==1){
            rabbitTemplate.convertAndSend("item.topic","item.insert",id);
        }else if (status==2){
            rabbitTemplate.convertAndSend("item.topic","item.delete",id);
        }
    }

    //修改商品
    @PutMapping()
    public void update(@RequestBody Item item){
        item.setUpdateTime(this.getCurrentTime());
        boolean update = itemService.updateById(item);
        log.info("更新结果:{}",update?"成功":"失败");
    }

    //根据id删除商品（直接删除，不做逻辑删除）
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        boolean delete = itemService.removeById(id);
        log.info("删除结果:{}",delete?"成功":"失败");
    }

    //获取系统当前时间
    private Date getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());
        Date date = null;
        try {
            date = sdf.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据id添加对应的库存
     */
    @GetMapping("stockRollBack/{itemId}/{num}")
    public void rollBackItem(@PathVariable("itemId")Long itemId,@PathVariable("num") Integer num){
        if (itemId == null) {
            throw new RuntimeException("id不能为null");
        }
        itemService.rollBackItem(itemId, num);
    }
}
