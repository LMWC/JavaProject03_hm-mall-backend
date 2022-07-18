package com.hmall.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableFeignClients(basePackages = "com.hmall.common.client",defaultConfiguration = FeignLogConfiguration.class)
@EnableFeignClients(basePackages = "com.hmall.common.client",defaultConfiguration = {
        FeignLogConfiguration.class,MyFeignInterceptor.class
})
public class FeignApiAutoConfig {
}
