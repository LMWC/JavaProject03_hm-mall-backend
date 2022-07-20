package com.hmall.common.client;

import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("itemservice")
public interface ItemClient {
    @GetMapping("/item/list")
    PageDTO<Item> queryItemByPage(@RequestParam("page") int page, @RequestParam("size") int size);

    @GetMapping("/item/{id}")
    Item queryItemById(@PathVariable("id") Long id);

    //更新商品
    @PutMapping("/item")
    public void update(@RequestBody Item item);

    /**
     * 根据id加对应的库存
     * @param itemId
     * @param num
     */
    @GetMapping("/item/stockRollBack/{itemId}/{num}")
    void rollBackItem(@PathVariable("itemId")Long itemId,@PathVariable("num") Integer num);
}
