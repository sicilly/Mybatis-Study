package com.sicilly.dao;

import com.sicilly.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    // 插入数据
    int addBlog(Blog blog);

    // 根据条件查询博客
    List<Blog> queryBlogIF(Map map);
}
