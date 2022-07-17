package com.hmall.order.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 虎哥
 */
@Data
@TableName("tb_order_logistics")
public class OrderLogistics{
    /**
     * 订单id，与订单表一对一
     */
    @TableId(type = IdType.INPUT)
    private Long orderId;
    /**
     * 物流单号
     */
    private String logisticsNumber;
    /**
     * 物流名称
     */
    private String logisticsCompany;
    /**
     * 收件人
     */
    private String contact;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String town;
    /**
     * 街道
     */
    private String street;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}