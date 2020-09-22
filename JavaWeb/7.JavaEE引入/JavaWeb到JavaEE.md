# JavaWeb到JavaEE

## 三层架构

![image-20200617194453732](C:\Users\q1367\Desktop\JavaWeb\7.JavaEE引入\JavaEE三层架构.png)

### 经典包架构

| 层级         | 经典命名                            | 补充说明            |
| ------------ | ----------------------------------- | ------------------- |
| web层        | com.atguigu.web/servlet/controller  |                     |
| service层    | com.atguigu.service                 | service接口包       |
|              | com.atguigu.service.impl            | service接口包实现类 |
| dao持久层    | com.atguigu.dao                     | Dao接口包           |
|              | com.atguigu.dao.impl                | Dao接口实现类       |
| 实体bean对象 | com.atguigu.pojo/entity/domain/bean | JavaBean类          |
| 测试包       | com.atguigu.test/junit              |                     |
| 工具类       | com.atguigu.utils                   |                     |

## 书城项目

### 步骤

#### **1、先创建书城需要的数据库和表。**

```sql
DROP DATABASE IF EXISTS book;

CREATE DATABASE book;

USE book;

CREATE TABLE t_user(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`username` VARCHAR(20) NOT NULL UNIQUE,
`password` VARCHAR(32) NOT NULL,
`email` VARCHAR(200)
)

INSERT INTO t_user(`username`, `password`,`email`) VALUES ('admin', 'admin', 'admin@atguigu.com');

SELECT * FROM t_user;
```

#### **2、编写数据库对应的JavaBean对象**

```java
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    ……
```

#### **3、编写工具类 JdbcUtils**

工具类主要负责为业务提供基于数据库连接池的链接获取和关闭服务，我们使用Druid来处理服务器相关。

#### **4、编写DAO**

- 应对DAO建立正确的认识，DAO负责的是为Service层提供与数据库交互的入口，因此在这里封装和数据库相关的一些方法。
- 对于DAO的实现类，有一些通用的，比如说单条查询、多记录查询、特殊值查询、通用增删改等功能，可以用一个BaseDAO把基础功能封装起来，所有DAO实现类都继承于BaseDAO去实现对应的DAO接口，使得代码结构更加清晰，复用性好。

![](C:\Users\q1367\Desktop\JavaWeb\7.JavaEE引入\BaseDAO示例.png)

- 比如说DAO当中queryUserByUsername，虽然我们清楚这是为了给用户检测用户名是否可用，但是在命名上，DAO还是要以query、update等具有数据库色彩的名称来命名。

- 因为UserService层需要登录、注册、验证，所以DAO要提供queryUserByUsernameAndPassword、insertUser、queryUserByUsername。

  ![image-20200619121504407](C:\Users\q1367\Desktop\JavaWeb\7.JavaEE引入\DAO接口.png)

```java
public class UserDAOImpl extends BaseDAO<User> implements UserDAO {

    @Override
    public boolean insertUser(String username, String password, String email) {
        Connection conn = JdbcUtils.getConnection();
        String sql = "insert into t_user (`username`, `password`, `email`) values (?,?,?)";
        int updateCount = update(conn, sql, username, password, email);
        JdbcUtils.close(conn);
        return updateCount != -1;
    }

    @Override
    public User queryUserByUsername(String username) {
        Connection conn = JdbcUtils.getConnection();
        String sql = "select * from t_user where username = ?";
        User user = singleQuery(conn, sql, username);
        JdbcUtils.close(conn);
        return user;
    }

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        Connection conn = JdbcUtils.getConnection();
        String sql = "select * from t_user where username = ? and password = ?";
        User user = singleQuery(conn, sql, username, password);
        JdbcUtils.close(conn);
        return user;
    }
}
```

#### **5、Service层的建立**

原则：按照应用中对应模块所需要的具体功能，依赖于对应的DAO，做接口设计与具体实现。

![image-20200619121816663](C:\Users\q1367\Desktop\JavaWeb\7.JavaEE引入\UserService接口设计.png)

```java
public class UserServiceImpl implements UserService
{
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public boolean register(String username, String password, String email) {
        return userDAO.insertUser(username, password, email);
    }

    @Override
    public boolean validateUsername(String username) {
        return userDAO.queryUserByUsername(username) != null;
    }

    @Override
    public boolean login(String username, String password) {
        return userDAO.queryUserByUsernameAndPassword(username, password) != null;
    }
}
```

#### **6、Web层的建立**

Web层，即Web应用中API的直接实现。

依赖于Service层为我们提供的功能，和客户端进行功能对接。

```java
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1、从表单中获取参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repwd = request.getParameter("repwd");
        String email = request.getParameter("email");
        String code = request.getParameter("code");
        // 2、验证验证码是否正确，先写死
        if ("abcde".equalsIgnoreCase(code)) {
            if (!userService.validateUsername(username)) {
                if (password.equals(repwd)) {

                    //用户名可用，才能注册
                    userService.register(username, password, email);
                    request.getRequestDispatcher("/pages/user/regist_success.html").forward(request, response);
                } else {
                    request.getRequestDispatcher("/pages/user/regist.html").forward(request, response);
                    System.out.println("验证密码不正确");
                }
            } else {
                System.out.println("??");
                request.getRequestDispatcher("/pages/user/regist.html").forward(request, response);
            }
        } else {
            //验证码没通过，返回注册页面
            request.getRequestDispatcher("/pages/user/regist.html").forward(request, response);
        }
    }

}
```

