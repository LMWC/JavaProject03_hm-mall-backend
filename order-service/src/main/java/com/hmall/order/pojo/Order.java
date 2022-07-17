package com.hmall.order.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_order")
public class Order{
    /**
     * 订单编号
     */
    @TableId(type = IdType.INPUT)
    private Long id;
    /**
     * 商品金额
     */
    private Long totalFee;
    /**
     * 付款方式：1:微信支付, 2:支付宝支付, 3:扣减余额
     */
    private Integer paymentType;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单状态,1、未付款 2、已付款,未发货 3、已发货,未确认 4、确认收货，交易成功 5、交易取消，订单关闭 6、交易结束
     */
    private Integer status;
    /**
     * 创建订单时间
     */
    private Date createTime;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 发货时间
     */
    private Date consignTime;
    /**
     * 确认收货时间
     */
    private Date endTime;
    /**
     * 交易关闭时间
     */
    private Date closeTime;
    /**
     * 评价时间
     */
    private Date commentTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}