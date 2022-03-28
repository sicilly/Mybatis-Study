# Mybatis-Study
## mybatis-01
### 0. 准备工作
创建mybatis数据库,创建user表，增加id,name,pwd三个字段，增加一些数据

工具类com/sicilly/utils/MybatisUtils.java
```java
package com.sicilly.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            //使用mybatis第一步：获取sqlSessionFactory对象
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。
    // 你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句

    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
}

```

实体类com/sicilly/pojo/User.java注意和数据表中字段一一对应
```java
package com.sicilly.pojo;

public class User {
    private int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}

```

在pom.xml中引入mybatis、junit、mysql-connector-java的包
```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>


    <groupId>com.sicilly</groupId>
    <artifactId>Mybatis-Study</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>mybatis-01</module>
    </modules>


    <dependencies>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>


        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>


    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>

</project>
```
### 1. 第一个Mybatis程序(查询)
com/sicilly/dao/UserMapper.java  是一个接口
```java
package com.sicilly.dao;

import com.sicilly.pojo.User;

import java.util.List;

public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();
}

```
com/sicilly/dao/UserMapper.xml  绑定接口，在这里写sql语句
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.sicilly.dao.UserMapper">

<!--    查询语句 id就是对应的namespace中的方法名-->
<!--    resultType是sql语句执行的返回值-->
    <select id="getUserList" resultType="com.sicilly.pojo.User">
        select * from mybatis.user
    </select>

</mapper>
```

### 增删查改实现

#### 根据id查

UserMapper.java写接口

```java
package com.sicilly.dao;

import com.sicilly.pojo.User;

import java.util.List;

public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();

    // 根据ID查询用户
    User getUserById(int id);
}

```

UserMapper.xml 补充实现类 写sql

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.sicilly.dao.UserMapper">

    <select id="getUserById" parameterType="int" resultType="com.sicilly.pojo.User">
        select * from mybatis.user where id=#{id}
    </select>

</mapper>
```

UserDaoTest.java 测试

```java
package com.sicilly.dao;

import com.sicilly.pojo.User;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {

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
}

```

#### 新增用户

UserMapper.java

这次要传入的是User对象而不是基本类型int了

```java
package com.sicilly.dao;

import com.sicilly.pojo.User;

import java.util.List;

public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();

    // 根据ID查询用户
    User getUserById(int id);

    // 新增用户 传入的是一个user对象
    int addUser(User user);
}

```



UserMapper.xml

注意如何取出对象中的属性

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.sicilly.dao.UserMapper">


<!--User对象中的属性，可以直接取出来-->
    <insert id="addUser" parameterType="com.sicilly.pojo.User">
        insert into mybatis.user (id,name,pwd) values(#{id},#{name},#{pwd})
    </insert>

</mapper>
```



UserDapTest.java

注意要提交事务！

```java
import java.util.List;

public class UserDaoTest {

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
}

```

#### 修改、删除用户

UserMapper.java

```
package com.sicilly.dao;

import com.sicilly.pojo.User;

import java.util.List;

public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();

    // 根据ID查询用户
    User getUserById(int id);

    // 新增用户 传入的是一个user对象
    int addUser(User user);

    // 修改用户
    int updateUser(User user);
    
    // 删除用户
    int deleteUser(int id);
}
```

UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--namespace绑定一个对应的Mapper接口-->
<mapper namespace="com.sicilly.dao.UserMapper">

    <update id="updateUser" parameterType="com.sicilly.pojo.User">
        update mybatis.user
        set name = #{name},pwd=#{pwd}
        where id=#{id};
    </update>
    
    <delete id="deleteUser" parameterType="int">
        delete
        from mybatis.user
        where id=#{id};

    </delete>    

</mapper>
```

UserDapTest.java

```java
package com.sicilly.dao;

import com.sicilly.pojo.User;
import com.sicilly.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {

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

```

