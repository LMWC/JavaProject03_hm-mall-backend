package com.hmall.user.web;

import com.hmall.user.pojo.Address;
import com.hmall.user.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @GetMapping("{id}")
    public Address findAddressById(@PathVariable("id") Long id) {
        return addressService.getById(id);
    }

    @GetMapping("/uid/{userId}")
    public List<Address> findAddressByUserId(@PathVariable("userId") Long userId) {
        return addressService.query().eq("user_id", userId).list();
    }
}
