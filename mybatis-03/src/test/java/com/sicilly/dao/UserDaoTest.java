package com.sicilly.dao;

import com.sicilly.pojo.User;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;


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

}
