package com.hmall.user.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_address")
public class Address {
    private Long id;
    private Long userId;
    private String contact;// 收件人姓名
    private String mobile;// 电话
    private String province;// 省份
    private String city;// 城市
    private String town;// 区
    private String street;// 街道地址
    private Boolean isDefault;
}
