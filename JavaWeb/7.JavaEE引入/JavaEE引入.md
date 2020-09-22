# JavaEE引入

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

1、先创建书城需要的数据库和表。

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

2、编写数据库对应的JavaBean对象

```java
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    ……
```

3、编写工具类 JdbcUtils