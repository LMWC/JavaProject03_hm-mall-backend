package com.hmall.order.interceptor;

import com.hmall.order.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入用户鉴权拦截器...");
        String authorization = request.getHeader("authorization");
        if (StringUtils.isEmpty(authorization)){
            log.info("非法请求,地址:{}",request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);//403
            return false;
        }
        UserHolder.setUser(Long.parseLong(authorization));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserHolder.removeUser();
    }
}
