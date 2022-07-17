package com.hmall.item.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;

    //根据id查询商品
    @GetMapping("/{id}")
    public Item getById(@PathVariable("id") Long id){
        Item item = itemService.getById(id);
        return item;
    }

    //分页查询商品(基于mysql)
    @GetMapping("/list")
    public PageDTO pageQuery(@RequestParam int page, @RequestParam int size){
        Page<Item> pageInfo = new Page<>(page,size);
        itemService.page(pageInfo);

        return new PageDTO(pageInfo.getTotal(),pageInfo.getRecords());
    }

    //新增商品
    @PostMapping
    public void add(@RequestBody Item item){
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        itemService.save(item);
    }

    //上架、下架商品
    @PutMapping("/status/{id}/{status}")
    public void status(@PathVariable("id") Long id,@PathVariable("status") int status){
        Item item = itemService.getById(id);
        item.setStatus(status);
        itemService.updateById(item);
    }

    //修改商品
    @PutMapping()
    public void update(@RequestBody Item item){
        itemService.updateById(item);
    }

    //根据id删除商品（直接删除，不做逻辑删除）
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        itemService.removeById(id);
    }
}
