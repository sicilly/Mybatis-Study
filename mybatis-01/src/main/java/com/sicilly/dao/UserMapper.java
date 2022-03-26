package com.sicilly.dao;

import com.sicilly.pojo.User;

import java.util.List;

public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();
}
