package com.hmall.search.doc;

import com.hmall.common.dto.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//文档对应的Java类
@Data
@NoArgsConstructor
public class ItemDoc {
    private Long id;//商品id
    private String name;//商品名称
    private Long price;//价格（分）
    private Integer stock;//库存数量-------
    private String image;//商品图片
    private String category;//分类名称
    private String brand;//品牌名称
    private String spec;//规格---------
    private Integer sold;//销量
    private Integer commentCount;//评论数
    private Integer status;//是否下架--------
    private Boolean isAD;//是否广告--------
    private Date createTime;//创建时间
    private Date updateTime;//更新时间

    //自动补全，设置对应字段
    private List<String> suggestion;

    public ItemDoc(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.image = item.getImage();
        this.category = item.getCategory();
        this.brand = item.getBrand();
        this.spec = item.getSpec();
        this.sold = item.getSold();
        this.commentCount = item.getCommentCount();
        this.status = item.getStatus();
        this.isAD = item.getIsAD();
        this.createTime = item.getCreateTime();
        this.updateTime = item.getUpdateTime();
        this.suggestion = Arrays.asList(this.category,this.brand);
    }
}
