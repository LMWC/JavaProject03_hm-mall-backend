package com.hmall.common.client;

import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("itemservice")
public interface ItemClient {
    @GetMapping("/item/list")
    PageDTO<Item> queryItemByPage(@RequestParam("page") int page, @RequestParam("size") int size);

    @GetMapping("/item/{id}")
    Item queryItemById(@PathVariable("id") Long id);
}
