package com.hmall.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.user.mapper.AddressMapper;
import com.hmall.user.pojo.Address;
import com.hmall.user.service.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}