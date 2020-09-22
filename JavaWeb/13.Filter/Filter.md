# Filter

## Filter过滤器定义

1. 它是JavaEE三大标准之一
2. 是标准，即为接口
3. 功能总结:**拦截请求，过滤响应**

**常见应用场景：**

1、权限检查

2、日记操作

3、事务管理

## Filter的使用

例子：admin文件夹下的资源需要登录用户才能访问。



步骤一：创建实现Filter接口的Filter类

步骤二：实现最为重要的   **doFilter**  方法

步骤三：到web.xml中去配置  **filter**  的拦截路径

## Filter的生命周期

Filter的生命周期所包含的方法：

1. 构造器方法
2. init初始化方法
3. doFilter过滤方法
4. destroy销毁方法

Filter的初始化是饿汉式的，在服务器启动的时候会先执行构造器方法与init初始化方法。

每次拦截到请求，就会执行doFilter过滤方法。

在工程停止运行的时候，会执行销毁方法。

## FilterConfig类

FilterConfig类见名知义，它是Filter过滤器的配置文件类。

Tomcat每次创建Filter的时候，也会同时创建一个FilterConfig类，这里面包含了Filter类的配置信息。

FilterConfig类的作用是获取Filter过滤器的配置内容

1. 获取Filter的名称filter-name内容
2. 获取在Filter中配置的init-param初始化参数
3. 获取ServletContext对象

## FilterChain类

Filter	过滤器

FilterChain	过滤器链（多个过滤器一起工作



在Fiter的doFilter方法中，最后我们会进行

**chain.doFilter()**方法，其作用为：

1. 执行下一个Filter过滤器（如果有Filter
2. 执行目标资源（没有Filter

**在执行多个Filter过滤器时，他们的执行先后顺序是根据在web.xml中配置的前后关系决定的。**

### 多个Filter执行的特点

1. 所有的filter和目标资源默认都执行在同一个线程中。
2. 以上多个filter共同执行的时候，他们都是用同一个Request对象。

![image-20200630122537910](C:\Users\q1367\Desktop\JavaWeb\13.Filter\图解多Servlet执行.png)

## Filter过滤器的拦截路径

### 三种方法

#### 精确匹配

eg:

```xml
<url-pattern>/target.jsp</url-pattern>
```

以上匹配的路径，精确到http://ip:port/工程路径/target.jsp

#### 目录匹配

eg:

```xml
<url-pattern>/admin/*</url-pattern>
```

以上匹配的路径，精确到http://ip:port/工程路径/admin/   

目录下的所有资源

#### 后缀名匹配

eg:

```xml
<url-pattern>*.jpg</url-pattern>
```

以上匹配的路径，表示工程下任何以jpg结尾的资源都要被过滤器处理。

### 拦截原则

拦截只关心抵制是否匹配，而不关心资源是否存在！