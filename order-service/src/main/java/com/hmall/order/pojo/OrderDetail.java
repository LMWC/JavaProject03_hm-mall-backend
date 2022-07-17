package com.hmall.order.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_order_detail")
public class OrderDetail {
    /**
     * 订单编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单编号
     */
    private Long orderId;
    /**
     * 商品id
     */
    private Long itemId;
    /**
     * 商品购买数量
     */
    private Integer num;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品单价
     */
    private Long price;
    /**
     * 商品规格数据
     */
    private String spec;
    /**
     * 图片
     */
    private String image;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}