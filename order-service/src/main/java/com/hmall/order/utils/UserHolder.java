package com.hmall.order.utils;

public class UserHolder {
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    public static void setUser(Long userId) {
        tl.set(userId);
    }

    public static Long getUser() {
        return tl.get();
    }
    //清除内容等待gc
    public static void removeUser(){
        tl.remove();
    }
}
