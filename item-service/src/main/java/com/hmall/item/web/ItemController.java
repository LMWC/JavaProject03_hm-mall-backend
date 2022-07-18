package com.hmall.item.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;

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
    @PutMapping("/status/{id}/{status}")
    public void status(@PathVariable("id") Long id,@PathVariable("status") int status){
        Item item = itemService.getById(id);
        item.setStatus(status);
        boolean update = itemService.updateById(item);
        log.info("上下架结果:{}",update?"成功":"失败");
    }

    //修改商品
    @PutMapping()
    public void update(@RequestBody Item item){
        boolean update = itemService.updateById(item);
        log.info("更新结果:{}",update?"成功":"失败");
    }

    //根据id删除商品（直接删除，不做逻辑删除）
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        boolean delete = itemService.removeById(id);
        log.info("删除结果:{}",delete?"成功":"失败");
    }
}
