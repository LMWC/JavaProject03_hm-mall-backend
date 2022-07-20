package com.hmall.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmall.item.pojo.Item;
import org.apache.ibatis.annotations.Update;

public interface ItemMapper extends BaseMapper<Item> {
    /**
     * 根据id加对应的库存
     * @param itemId
     * @param num
     */
    @Update("update tb_item set stock = stock + ${num} where id = #{itemId}")
    void rollBackItem(Long itemId, Integer num);
}
