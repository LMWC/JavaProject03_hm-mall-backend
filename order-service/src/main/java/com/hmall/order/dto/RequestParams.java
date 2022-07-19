package com.hmall.order.dto;

import lombok.Data;

@Data
public class RequestParams {
    private int num;
    private int paymentType;
    private Long addressId;
    private Long itemId;
}
