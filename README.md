# Mybatis-Study
## 增删改查(mybatis-01)
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

import com.User;

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
<mapper namespace="com.UserMapper">

<!--    查询语句 id就是对应的namespace中的方法名-->
<!--    resultType是sql语句执行的返回值-->
    <select id="getUserList" resultType="com.User">
        select * from mybatis.user
    </select>

</mapper>
```

### 2. 增删查改实现

#### 根据id查

UserMapper.java写接口

```java
package com.sicilly.dao;

import com.User;

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
<mapper namespace="com.UserMapper">

    <select id="getUserById" parameterType="int" resultType="com.User">
        select * from mybatis.user where id=#{id}
    </select>

</mapper>
```

UserDaoTest.java 测试

```java
package com.sicilly.dao;

import com.User;
import com.MybatisUtils;
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

import com.User;

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
<mapper namespace="com.UserMapper">


<!--User对象中的属性，可以直接取出来-->
    <insert id="addUser" parameterType="com.User">
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

```java
package com.sicilly.dao;

import com.User;

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
<mapper namespace="com.UserMapper">

    <update id="updateUser" parameterType="com.User">
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

import com.User;
import com.MybatisUtils;
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

### 3. 用Map传递参数

对象传递参数，直接在sql中取对象的属性即可 `parameterType=“Object”`

只有一个基本类型参数的情况下，可以直接在sql中取到

多个参数可以用map或者注解（后面讲）传递参数，直接在sql中取出key即可 `parameterType=“map”`

UserMapper.java

```java
    // 使用map来传递参数
    int addUser2(Map<String,Object> map);
```

UserMapper.xml

```xml
    <insert id="addUser2" parameterType="map">
        insert into mybatis.user (id,name,pwd)
        values (#{id1}, #{name1}, #{pwd1});
    </insert>
```

UserDapTest.java

```java
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

```

## 配置解析(mybatis-02)

### 1. 核心配置文件

- mybatis-config.xml

```xml
configuration（配置）
    properties（属性）
    settings（设置）
    typeAliases（类型别名）
    typeHandlers（类型处理器）
    objectFactory（对象工厂）
    plugins（插件）
        environments（环境配置）
            environment（环境变量）
            transactionManager（事务管理器）
    dataSource（数据源）
    databaseIdProvider（数据库厂商标识）
    mappers（映射器）
```



### 2. 环境配置（environments）

 MyBatis 可以配置成适应多种环境 

 **不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。** 

Mybatis 默认的事务管理器是JDBC，连接池：POOLED



### 3. 属性

我们可以通过properties属性来引用配置文件

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。 （db.properties）

编写一个配置文件

db.properties

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?userSSL=true&useUnicode=true&characterEncoding=UTF-8
username=root
password=
```

在核心配置文件中引入

mybatis-config.xml (同时有的话，优先走外面properties)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

    <!--引入外部配置文件-->
    <!--<properties resource="db.properties"/>-->

    <properties resource="db.properties">
        <property name="username" value="root"></property>
        <property name="password" value="hdk123"></property>
    </properties>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>

    <!--每一个mapper.xml都需要注册-->
    <mappers>
        <mapper resource="com/sicilly/dao/UserMapper.xml"/>
    </mappers>

</configuration>
```



### 4. 类型别名（typeAliases）

 类型别名可为 Java 类型设置一个缩写名字。 

```xml
<typeAliases>
    <typeAlias type="com.User" alias="User"></typeAlias>
</typeAliases>
```

扫描实体类的包，默认别名就为这个类的类名首字母小写

```xml
<typeAliases>
    <package name="com.User"></package>
</typeAliases>
```

在实体类，比较少的时候使用第一种，实体类多使用第二种。

第一种可以自定义，第二则不行，但是 若有注解，则别名为其注解值 。

```java
@Alias("hello")
public class User {
}
```



### 5. 设置

| 设置名             | 描述                                                         | 有效值                                                       | 默认值 |
| :----------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| cacheEnabled       | 全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。     | true \| false                                                | true   |
| lazyLoadingEnabled | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。 | true \| false                                                | false  |
| logImpl            | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。        | SLF4J \| LOG4J \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | 未设置 |

### 6. 其他

- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
- [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)
  - mybatis-generator-core
  - mybatis-plus
  - 通用mapper

### 7. 映射器

方式一: [推荐使用]

```xml
<mappers>
    <mapper resource="com/sicilly/dao/UserMapper.xml"/>
</mappers>
```

方式二：

```xml
<mappers>
    <mapper class="com.UserMapper" />
</mappers>
```

- 接口和它的Mapper必须同名
- 接口和他的Mapper必须在同一包下

方式三：

```xml
<mappers>
    <package name="com.sicilly.dao" />
</mappers>
```

- 接口和它的Mapper必须同名
- 接口和他的Mapper必须在同一包下

### 8.生命周期和作用域

作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

**SqlSessionFactoryBuilder**: 

-  一旦创建了 SqlSessionFactory，就不再需要它了 。
- 局部变量

 **SqlSessionFactory**：

-  就是数据库连接池。
-  一旦被创建就应该在应用的运行期间一直存在 ，**没有任何理由丢弃它或重新创建另一个实例 。** 多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。 
-  因此 SqlSessionFactory 的最佳作用域是应用作用域。 
-  最简单的就是使用单例模式或者静态单例模式。 

 **SqlSession**：

- 每个线程都应该有它自己的 SqlSession 实例。 
- 连接到连接池的请求！
-  SqlSession 的实例不是线程安全的，因此是不能被共享的 ，所以它的最佳的作用域是请求或方法作用域。 
- 用完之后赶紧关闭，否则资源被占用。

## 结果集映射(mybatis-03)

### 1. 问题

**属性名和字段名不一致的问题**

数据库中的字段---- pwd

实体类字段-----password

User

```java
package com.hou.pogo;

public class User {

    private int id;
    private String name;
    private String password;
}
```

运行结果：

> User{id=2, name='wang', password='null'}



### 2. 解决方法

核心配置文件

- 起别名

```xml
<select id="getUserById" resultType="User"
    parameterType="int">
        select id,name,pwd as password from mybatis.user where id = #{id}
</select>
```

- resultMap 结果集映射

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.sicilly.dao.UserMapper">

	<!-- resultMap结果集映射-->
    <resultMap id="UserMap" type="User">
        
		<!-- column数据库中的字段，property实体类中的属性-->
        <result column="id" property="id"></result>
        <result column="name" property="name"></result>
        <result column="pwd" property="password"></result>
        
    </resultMap>
    
	<!-- 使用resultMap属性-->
    <select id="getUserList" resultMap="UserMap">
        select * from mybatis.user
    </select>

</mapper>
```

- `resultMap` 元素是 MyBatis 中最重要最强大的元素。 

- ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。 

```xml
<resultMap id="UserMap" type="User">
    <!--colunm 数据库中的字段，property实体中的属性-->
    <!--<result column="id" property="id"></result>-->
    <!--<result column="name" property="name"></result>-->
    <result column="pwd" property="password"></result>
</resultMap>
```
