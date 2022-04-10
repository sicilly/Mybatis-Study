import com.sicilly.dao.BlogMapper;
import com.sicilly.pojo.Blog;
import com.sicilly.utils.IDUtils;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyTest {

    @Test
    public void addBlog(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);

        Blog blog = new Blog();
        blog.setId(IDUtils.getId());
        blog.setAuthor("sicilly");
        blog.setCreateTime(new Date());
        blog.setViews(999);
        blog.setTitle("first");

        blogMapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("second");
        blogMapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("third");
        blogMapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("forth");
        blogMapper.addBlog(blog);

        sqlSession.close();
    }

    @Test
    public void queryBlogIF(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        HashMap map=new HashMap();
//        map.put("title","first");
        map.put("author","sicilly");
        List<Blog> blogs = mapper.queryBlogIF(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }

        sqlSession.close();
    }
}