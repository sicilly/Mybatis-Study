package com.sicilly.dao;

import com.sicilly.pojo.User;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoTest {

    @Test
    public void test(){
        // 第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        try {
            // 方式一：getMapper
            // 先获得userMapper接口里的对象
            UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
            // 就可以执行对象里面的方法
            List<User> userList=userMapper.getUserList();

            for (User user: userList) {
                System.out.println(user);
            }
        }finally {
            // 关闭sqlSession
            sqlSession.close();
        }

    }

    @Test
    public void getUserById(){
        // 第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        try {
            // 方式一：getMapper
            // 先获得userMapper接口里的对象
            UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
            // 就可以执行对象里面的方法 查id为1的user
            User user=userMapper.getUserById(1);
            // 输出
            System.out.println(user);
        }finally {
            // 关闭sqlSession
            sqlSession.close();
        }

    }

    // 增删改需要提交事务
    @Test
    public void AddUser(){
        // 第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        try {
            // 方式一：getMapper
            // 先获得userMapper接口里的对象
            UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
            // 就可以执行对象里面的方法 增加用户
            userMapper.addUser(new User(4,"jerry","654321"));
            // 提交事务
            sqlSession.commit();
        }finally {
            // 关闭sqlSession
            sqlSession.close();
        }

    }

    @Test
    public void AddUser2(){
        // 第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        try {
            // 方式一：getMapper
            // 先获得userMapper接口里的对象
            UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
            // 准备一个万能的map
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("id1",10);
            map.put("name1","dd");
            map.put("pwd1","123");
            // 把map放进userMapper
            userMapper.addUser2(map);

            // 提交事务
            sqlSession.commit();
        }finally {
            // 关闭sqlSession
            sqlSession.close();
        }

    }

    @Test
    public void updateUser(){
        // 第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        try {
            // 方式一：getMapper
            // 先获得userMapper接口里的对象
            UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
            // 就可以执行对象里面的方法 修改用户
            userMapper.updateUser(new User(4,"tom","333"));
            // 提交事务
            sqlSession.commit();
        }finally {
            // 关闭sqlSession
            sqlSession.close();
        }

    }

    @Test
    public void deleteUser(){
        // 第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        try {
            // 方式一：getMapper
            // 先获得userMapper接口里的对象
            UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
            // 就可以执行对象里面的方法 删除用户
            userMapper.deleteUser(4);
            // 提交事务
            sqlSession.commit();
        }finally {
            // 关闭sqlSession
            sqlSession.close();
        }

    }
}
