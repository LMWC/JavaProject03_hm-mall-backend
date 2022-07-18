package com.hmall.common.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

//feign日志：bean配置形式
//这里不用@Configuration的原因，自动配置启动的时候已经把这个配置类加载里面了
public class FeignLogConfiguration {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.BASIC;
    }
}
