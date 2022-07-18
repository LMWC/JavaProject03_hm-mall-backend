package com.hmall.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.hmall.common.client",defaultConfiguration = FeignLogConfiguration.class)
public class FeignApiAutoConfig {
}
