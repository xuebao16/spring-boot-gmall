package com.ft.gmall.user.service;

import org.apache.dubbo.config.annotation.Service;

@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello() {
        return "Hello Gmall";
    }
}
