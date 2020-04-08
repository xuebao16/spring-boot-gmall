package com.ft.gmall.manage;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.ft.gmall.manage.mapper")
@EnableDubbo
public class GmallManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallManageServiceApplication.class, args);
    }

}
