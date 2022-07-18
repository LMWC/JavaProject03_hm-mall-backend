package com.hmall.search.dto;

import lombok.Data;

//搜索业务的请求参数
@Data
public class RequestParams {
    private int page;
    private int size;
    private String key;

    private String category;
    private String brand;
    private Long maxPrice;
    private Long minPrice;

    private String sortBy;
}
