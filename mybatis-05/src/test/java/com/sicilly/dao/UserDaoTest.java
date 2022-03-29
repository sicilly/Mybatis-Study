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
        // 获得sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try{
            // 反射
            UserMapper userMaper = sqlSession.getMapper(UserMapper.class);
            List<User> users= userMaper.getUsers();

            for (User user:users) {
                System.out.println(user);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //关闭
            sqlSession.close();
        }
    }



}
