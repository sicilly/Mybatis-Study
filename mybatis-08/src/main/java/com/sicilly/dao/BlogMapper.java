package com.sicilly.dao;

import com.sicilly.pojo.Blog;
import com.sicilly.utils.IDUtils;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;

public interface BlogMapper {
    // 插入数据
    int addBlog(Blog blog);
}
