## MyBatis

## 简介

专注于和数据库进行交互，即持久化层框架

工具：一些功能的简单封装

框架：某个领域问题的整体解决方案

MyBatis：SQL映射框架

Hibernate：ORM框架

### 批判原生jdbc

1. 步骤麻烦
2. **硬编码sql语句，造成持久化层和java编码的耦合**

### 批判Hibernate框架

1. 黑箱太过霸道，业务逻辑非常复杂的时候，定制sql捉襟见肘。
2. 需要多学习一种HQL
3. **HQL的设计变成了反模式，反而增加程序员的学习成本**
4. **全映射框架，部分字段映射很难，体现了反模式**

### 愿景

最好有一个框架支持定制化sql，而且还功能强大

sql具有动态性，不硬编码在代码中产生耦合

![image-20200719212139119](C:\Users\q1367\Desktop\MyBatis\批判.png)

### MyBatis优点

1. MyBatis将最重要的步骤抽离出来，可以人工定制，其余步骤交给框架自动化
2. 通过配置文件维护sql，解决了和java业务代码的耦合问题，通过热启动就可以使改动生效
3. 完美解决数据库的优化问题
4. MyBatis底层就是对jdbc的简单封装
5. 既解耦又具有自动化功能，半自动的持久化层框架 （轻量级框架

## 两个重要文件

1. 全局配置文件：mybatis-config.xml；指导mybatis正确运行的一些全局设置；
2. SQL映射文件：EmployeeDao.xml;相当于是对EmployeeDao接口的一个实现描述

#### 映射对象探究

发现我们从session中获取的mapper对象是代理对象，是MyBatis自动创建的

#### 生命周期探究

SqlSessionFactoryBuilder创建工厂，其生命周期只用于创建工厂时的语句，甚至不需要引用

SqlSessionFactory创建sqlSession对象，工厂只需要创建一个用于整个应用

SqlSession一次会话就需要一个对象

### 全局配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
<!--
properties，有spring中context：property-placeholder：引用外部配置文件
属性：resource、url
resource：从类路径下开始引用
url：引用磁盘路径或者网络路径的资源
封装属性：在这里可以自定义属性
<property></property>可以指定单个属性
-->
    <properties resource="db.properties">
    </properties>
<!--
settings:MyBatis中极为重要的调整设置，他们会改变MyBatis的运行时行为，下表描述了设置中各项的意图、默认值等
例子：mapUnderscoreToCamelCase 开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。
-->
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
<!--
类型别名
typeAliases：就是为一个javaBean起别名，别名默认就是类名（不区分大小写
可以进一步用alias属性指定自定义别名
-->
<!--
批量别名；package标签，对整个包下的javaBean起别名，默认别名就是类名
在批量起别名的情况下想自定义，对着javaBean特定注解@Alias即可
-->
<!--
实际：建议还是不起别名
而且mybatis已经为很多基本类型和包装类还有常见类型封装好了别名，避免冲突
-->
    <typeAliases>
        <typeAlias type="com.atguigu.pojo.Employee" alias="employee"/>
    </typeAliases>
<!--
typeHandlers：类型处理器
原生jdbc执行sql；或者封装结果集；也有同样的过程
-->
<!--
plugins：强大的插件，用动态代理侵入mybatis底层四大组件进行工作
executor、parameterhandler、resultsethandler、statementhandler
-->
<!--
environments配置环境们，其default属性可以表示当前使用哪个环境
内部封装的每一个environment才是具体的环境，用id标注不同的环境，比如test、development、release
每一个环境只支持两个标签：
    transactionManager
    dataSource
即每个环境都需要事务管理器和数据源
实际环境的事务管理和数据源都是spring管理
-->
<!--
databaseIdProvider:用来考虑数据库移植性
我们设定好的databaseid在具体的实现配置文件中的select/update等标签中作为databaseId属性传入
即可标识此sql是某个具体数据库环境下使用的
-->
    <databaseIdProvider type="DB_VENDOR">
    <!--        name:数据库厂商标识    value：给这个标识起一个好用的名字
        MySQL/Oracle
    -->
        <property name="" value=""/>
    </databaseIdProvider>
<!--

写好的sql映射文件需要使用mappers注册进来

-->
    <mappers>
<!--
    class:直接引用接口的全类名,这种方式需要xml和dao接口同目录，而且两个文件命名一致
        另一种用法：
    url:从磁盘或者网络路径引用
    resource=""在类路径下找sql映射文件
-->
        <mapper url=""/>
        <mapper class=""/>
    </mappers>
</configuration>
```

### SQL映射配置

这个文件中能写的标签：

```xml
<cache></cache>：和缓存有关
<cache-ref namespace=""/>：缓存有关
delete update insert select
parameterMap:废弃
resultMap:结果集映射，用来做自定义结果集的封装规则
sql:抽取可重用的sql
```

#### delete update insert select

##### 增删改

**能写的属性**

![image-20200720200603881](C:\Users\q1367\Desktop\MyBatis\增删改查属性表.png)

 **实现获取到插入数据后的自增id**

通过配置让mybatis将自增id赋值给传入的employee的id属性

```xml
<insert id="insertEmployee" useGeneratedKeys="true" keyProperty="id">
    insert into employee (id, name, address) value (#{id}, #{name}, #{address})
</insert>
```

**配置查询主键**

假如我们的主键不是自增的，那么可以搭配selectKey封装sql语句获取到主键的进度，将它赋值给主键

```xml
<insert id="insertEmployee02">
    <selectKey order="BEFORE" resultType="integer" keyProperty="id">
        select max(id)+1 from employee
    </selectKey>
    insert into employee (id, name, address) value (#{id}, #{name}, #{address})
</insert>
```

selectKey上的属性可以指定order和keyProperty，分别表示在sql运行前后进行赋值，和赋值给哪个属性

resultType表示查询之后mybatis需要转换成什么类型，要匹配我们在javabean中定义的属性类型

#### 查

针对查询的传参，大致有以下的现象

- 单个参数
  - 基本类型
    - 取值：#{随便写}
  - pojo
- 多个参数
  - 取值#{随便写}无效了
  - 可用：0,1(参数的索引)或者param1，param2(第n个参数)
  - 原因：只要传入了多个参数；mybatis会自动将这些参数封装在一个map中，封装时使用的key就是参数的索引he参数的第几个表示
    - Map<String,Object> map = new HashMap<>();
    - map.put("1",传入的值)
  - #{}实际上是从map中取值
  - **其实我们可以告诉mybatis，封装参数map的时候别乱来，使用我们指定的key**
    - 用@param注解入参即可，可以命名map中的key
- 传入了map
  - 原理和多个参数的情况一样，用#{}按key取值
- 传入了pojo
  - 直接#{pojo中的属性名}就可以拿到值

#### **参数拓展**

##### **参数传递**

如果有方法如下：

```java
public Employee retrieveEmployee(@param("id")Integer id, String name, Address address);
```

那么在取值的时候，我们有

Integer id -> #{id}

String name => #{param2}

在Address类中有一个location字段：String location -> #{param3.location}

使用级联属性

 ![image-20200720210643899](C:\Users\q1367\Desktop\MyBatis\参数传递.png)

##### **参数处理**

![image-20200720211039982](C:\Users\q1367\Desktop\MyBatis\参数处理.png)

![image-20200720211308877](C:\Users\q1367\Desktop\MyBatis\参数取值可传入的属性.png)

比如说给参数传递null值，如果我们使用的是oracle数据库，那就需要进一步制定jdbcType告诉oracle该如何转换此null

##### **参数取值方式**

#{属性名}:是参数预编译的方式，参数的位置都是用？替代，参数是预编译设置进去的；

​	不会有sql注入问题

${属性名}:直接和sql拼串



${}使用情况：sql只有参数位置是支持预编译的，如果我们需要动态传入访问的数据库的名字

**总结：**参数位置使用#{}，其余位置使用${}

##### 查询返回list

如果返回的是集合，我们resultType写的是集合里面元素的类型

##### 查询返回对象封装到map

**单条记录**

mybatis默认会把列名作为key，值作为value；把对象如此封装到map返回给方法

returnType设置为map即可，因为map是mybatis中的封装好的内建类型

**多条记录**

需要借助@MapKey()指定封装多个对象时，我们使用的作为key的属性

```java
@MapKey("id")
public Map<Integer, Employee> getAllEmpsReturnMap();
```

这样，我们就可以从mybatis获取到以id为key的map，存有所有employee对象

这个时候的returnType一定要规定成value对应的对象类型

##### 结果集封装

1. 按照列名和属性名一一对应的规则（不区分大小写
2. 如果不一一对应
   1. 开启驼峰命名法（aaa_bbb  aaaBbb
   2. 起别名
   3. **自定义封装规则**

**自定义结果集（resultMap）：**自己定义每一列数据和javaBean的映射规则

```xml
<!--
resultMap:自己定义每一列数据和javaBean的映射规则
type="":指定为哪个javaBean自定义封装规则；全类名
id="":唯一标识，让别名在后面引用
-->
    <resultMap id="com.atguigu.pojo.Cat" type="mycat">
<!--   指定主键列的对应规则
        column="id"，指定字段名为id的这一列是主键列
        property:指定cat的哪个属性封装id这一列数据
-->
        <id property="id" column="id"/>
<!--    普通列    -->
        <result property="name" column="cname"/>
        <result property="gender" column="cgender"/>
    </resultMap>
```

```xml
<!--
这里的resultType设置为pojo表示用的是默认的封装规则   属性列名一一对应
-->
    <select id="getAllCat" resultMap="mycat">
        select * from cat
    </select>
```

##### **联合查询**

使用级联属性封装联合查询出来的结果

```xml
<resultMap id="com.atguigu.pojo.Key" type="mykey">
    <id property="id" column="id"/>
    <result property="keyName" column="keyname"/>
    <!--级联属性法-->
    <!--<result property="lock.id" column="lid"/>-->
    <!--<result property="lock.lockName" column="lockname"/>-->
    <!--联合法-->
    <association property="lock" javaType="com.atguigu.pojo.Lock">
        <id property="id" column="lid"/>
        <id property="lockName" column="lockname"/>
    </association>  
</resultMap>
```

##### 关联关系

1-1关联：一个bean持有另一个bean的引用（一个钥匙开一把锁

1-n关联：一个bean持有另一个bean的集合引用（一把锁对应多把钥匙，外键放在钥匙端

n-n关联：用中间表维护关联关系

##### collection定义集合类型属性的封装

```xml
<collection property="key" ofType="com.atguigu.pojo.Key"></collection>
```

property指定哪个属性是集合属性

ofType指定集合里面元素的类型