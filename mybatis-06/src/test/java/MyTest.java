import com.sicilly.dao.TeacherMapper;
import com.sicilly.pojo.Teacher;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

public class MyTest {
    public static void main(String[] args) {
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacher(1);
        System.out.println(teacher);
        sqlSession.close();
    }
}
