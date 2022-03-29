package com.sicilly.dao;

import com.sicilly.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper {

//  注解
    @Select("select * from user")
    List<User> getUsers();
}
