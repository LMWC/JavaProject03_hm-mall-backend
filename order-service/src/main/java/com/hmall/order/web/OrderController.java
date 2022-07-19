package com.hmall.order.web;

import com.hmall.order.dto.RequestParams;
import com.hmall.order.pojo.Order;
import com.hmall.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {

   @Autowired
   private IOrderService orderService;

   @GetMapping("{id}")
   public Order queryOrderById(@PathVariable("id") Long orderId) {
      return orderService.getById(orderId);
   }

   //提交订单
   @PostMapping
   //public Long submitOrder(){
   public Long submitOrder(@RequestBody RequestParams requestParams){
      //return 123865420L;
      return orderService.submitOrder(requestParams);
   }
}
