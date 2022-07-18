package com.hmall.order.web;

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

   @PostMapping
   public Long submitOrder(){

      return 123865420L;
   }
}
