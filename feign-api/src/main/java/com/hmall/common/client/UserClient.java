package com.hmall.common.client;

import com.hmall.common.dto.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("userservice")
public interface UserClient {
    @GetMapping("/address/uid/{userId}")
    public List<Address> findAddressByUserId(@PathVariable("userId") Long userId);
}
