package com.sicilly.dao;

import com.sicilly.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();

    // 根据ID查询用户
    User getUserById(int id);

    // 新增用户 传入的是一个user对象
    int addUser(User user);

    // 使用map来传递参数
    int addUser2(Map<String,Object> map);

    // 修改用户
    int updateUser(User user);

    // 删除用户
    int deleteUser(int id);
}
