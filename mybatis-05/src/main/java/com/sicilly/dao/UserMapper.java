package com.sicilly.dao;

import com.sicilly.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    // 使用注解开发
    // 查询所有用户
    @Select("select * from user")
    List<User> getUsers();

    //方法存在多个参数，所有的参数必须加@Param
    // 根据id查用户
    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") int id);

    // 新增一个用户
    @Insert("insert into user (id, name, pwd) values" +
            "(#{id},#{name},#{password})")
    int addUser(User user);

    // 修改一个用户
    @Update("update user set name=#{name}, pwd=#{password} " +
            "where id=#{id}")
    int updateUser(User user);

    // 删除一个用户
    @Delete("delete from user where id=#{id}")
    int deleteUser(@Param("id") int id);
}
