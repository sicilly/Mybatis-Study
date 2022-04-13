import com.sicilly.dao.UserMapper;
import com.sicilly.pojo.User;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class MyTest {
    @Test
    public void test(){
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);

        User user=mapper.queryUserById(1);
        System.out.println(user);

        //mapper.updateUser(new User(2,"aaaa","bbbb"));
        sqlSession.clearCache();  // 手动清理缓存

        User user2=mapper.queryUserById(1);
        System.out.println(user2);

        System.out.println(user==user2);

        sqlSession.close();

    }
}
