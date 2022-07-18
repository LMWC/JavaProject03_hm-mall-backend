package com.hmall.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

//服务间调用没有设置鉴权请求头，配置服务间的调用的拦截器
public class MyFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        requestTemplate.header("authorization", String.valueOf(2));
    }
}
