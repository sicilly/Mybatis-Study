package com.sicilly.dao;

import com.sicilly.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    User getUserById(int i);

    // Limit分页
    List<User> getUserByLimit(Map<String, Integer> map);
}
