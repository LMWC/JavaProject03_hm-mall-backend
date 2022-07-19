package com.hmall.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class OrderApplicationTests {

    @Test
    void test(){
        String string = UUID.randomUUID().toString();
        System.out.println(string);
        String replace = string.replace("-", "");
        System.out.println(replace.substring(0,18));
    }
}
