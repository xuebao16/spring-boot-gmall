package com.ft.gmall.cart;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ft.gmall.cart.mapper")
@EnableDubbo
public class GmallCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallCartServiceApplication.class, args);
    }

}
