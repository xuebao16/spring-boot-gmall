package com.ft.gmall.user.service;

import com.ft.gmall.user.bean.UmsMember;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUsers();

    UmsMember getUserById(Integer userid);

    int addUser(UmsMember umsMember);

    int updateUser(UmsMember umsMember);

    int delectUserById(Integer userId);
}
