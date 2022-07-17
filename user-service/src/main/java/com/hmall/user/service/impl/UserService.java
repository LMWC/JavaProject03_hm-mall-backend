package com.hmall.user.service.impl;

import com.hmall.user.mapper.UserMapper;
import com.hmall.user.pojo.User;
import com.hmall.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

}