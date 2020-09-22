# Servlet

## 一、介绍

### 什么是Servlet？

1、Servlet是JavaEE规范之一，规范就是接口。

2、Servlet是JavaWeb三大组件之一。三大组件分别是：

​	Servlet程序

​	Filter过滤器

​	Listener监听器

3、Servlet是运行在服务器上的一个Java小程序，它可以接收客户端发送过来的请求，并响应数据给客户端。

## 二、上手与使用

### 手动实现servlet程序

1、编写一个类去实现Servlet接口

2、实现service方法，处理请求，并相应数据

3、到web.xml中去配置servlet程序的访问地址

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
<!--    servlet标签给Tomcat服务器配置servlet程序-->
    <servlet>
<!--        servlet-name标签给servlet程序起一个别名，一般是类名-->
        <servlet-name>HelloServlet</servlet-name>
<!--        servlet-class是全类名-->
        <servlet-class>com.atguigu.servlet.HelloServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>HelloServlet</servlet-name>
<!--        给servlet程序配置访问地址
            / 斜杠表示在服务器解析的时候，表示地址为http://ip:port/工程路径
            /hello 则表示http://ip:port/工程路径/hello,映射到我们写好的小程序
            不以斜杠开头，则出现映射错误
-->
        <url-pattern>/hello</url-pattern>
    </servlet-mapping>

</web-app>
```

### 从URL到Servlet的响应过程

![](C:\Users\q1367\Desktop\JavaWeb\5.Servlet\响应图解.png)

### Servlet的生命周期

**1、执行Servlet构造器**

**2、执行init初始化方法**

**第一二步，是在第一次访问时候创建servlet程序会调用**

**3、执行service方法**

**第三步每次访问都会调用。**

**4、执行destroy方法**

**停止tomcat实例时，才会销毁servlet**

### 实际开发中，一般通过继承HttpServlet来实现Servlet接口

**1、编写一个继承于HttpServlet的类**

**2、根据业务需要重写doGet或者doPost方法**

**3、去web.xml中配置servlet访问**

### IDEA直接生成Servlet程序

![image-20200616183809024](C:\Users\q1367\Desktop\JavaWeb\5.Servlet\IDEA配置Servlet.png)

## 三、Servlet体系

### 继承体系

![image-20200616191634380](C:\Users\q1367\Desktop\JavaWeb\5.Servlet\Servlet继承体系.png)

### ServletConfig类

****

此类负责管理Servlet程序的配置信息

#### 三大作用

1、可以获取Servlet程序的别名servlet-name的值

2、获取初始化参数init-param

3、获取servletContext对象

#### 补充说明

Servlet程序和ServletConfig对象都是由Tomcat负责创建，我们负责使用

Servlet是懒汉式的，ServletConfig会根据Servlet的创建而创建

重写Servlet方法的init的时候，需要调用父类的init方法（GenericServlet中实现了），以绑定config

### ServletContext类

****

#### 介绍

1、ServletContext是一个接口，它表示Servlet上下文对象

2、一个web工程，只有一个ServletContext对象实例。

3、ServletContext对象是一个域对象。

##### 什么是域对象？

域对象，是可以像Map一样存取数据的对象，叫域对象。

这里的域指的是存取数据的操作范围。

![image-20200616200715836](C:\Users\q1367\Desktop\JavaWeb\5.Servlet\域对象与Map的区别.png)

#### 四个作用

1、获取web.xml中配置的上下文参数context-param

2、获取当前的工程路径，格式: /工程路径

3、获取工程部署后在服务器硬盘上的绝对路径

4、像Map一样存取数据

### HttpServletRequest类

****

#### 作用

​	· 每次只要有请求进入Tomcat服务器，服务器就会把请求过来的HTTP协议内容封装到Request类中，然后把对象传递到service方法(doGet/doPost)中给我们使用。

​	· 我们可以通过HttpServletRequest对象，获取到所有请求的信息。

#### 常用方法

![image-20200617124333732](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\HttpServletRequest常用方法.png)

#### POST方法下的参数编码问题

需要在提取参数前，调用	req.setCharacterEncodind("UTF-8");	设置编码。

#### 请求的转发

**什么是请求转发？**

服务器收到请求后，从一个资源跳转到另一个资源的操作（同一个服务器）。

**图解**

![image-20200617155942952](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\请求转发图解.png)

#### base标签的作用

无论当前页面在浏览器中是通过什么方式到达的（servlet跳转，目录直接获取……）

所有的a标签在进行跳转时都会参照base标签中的href属性值进行资源寻路。

![image-20200617165621201](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\base标签使用图例.png)

##### a标签href进阶

href内可以接URL也可以接URI。

当我们想使用URI来超链接资源时，地址以 **/** 开头，代表从 **协议://ip:port/** 开始；

​															地址直接以资源开头，代表从 **协议://ip:port/工程路径/** 开始。

#### JavaWeb中的绝对路径与相对路径问题

![image-20200617165844174](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\路径图解.png)

#### JavaWeb中的/问题

![image-20200617170252969](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\斜杠问题图解.png)

### HttpServletResponse类

****

​	· 每次只要有请求进入Tomcat服务器，Tomcat服务器除了封装Request信息到相应的类之外，会把服务器的响应Response也封装到一个Response类中，然后把对象传递到service方法(doGet/doPost)中给我们使用。

​	· 我们可以通过HttpServletResponse对象，获取到所有响应的信息。

#### 通过流机制工作

从HttpServletResponse中获取数据我们需要利用到两个流对象。



字节流			getOutputStream()				 处理二进制文件，常用于下载

字符流			getWriter()								处理回传的字符串，最常用的方式

**对于一个HSR对象，我们一次只能从中获取一个流（排他性）**

#### 解决响应的中文乱码问题

**方案一：**

1、在服务器端，设置resp.setCharacterEncoding("UTF-8");

2、还需要告诉浏览器，本次响应使用的是什么编码，chrome默认使用GBK

​	resp.setHeader("Content-Type", "text/html; charset=UTF-8");

**方案二：**

resp.setContentType("text/html; charset=UTF-8");

**他会同时设置服务器和客户端（设置了响应头）都使用UTF-8字符集。**

**此方法一定要在获取流对象之前调用才有效。**

#### 请求重定向

​	请求重定向，是指客户端给服务器发请求，然后服务器告诉客户端，让客户端去新地址访问，叫请求重定向。（因为之前的地址可能已经被废弃）

![image-20200617184323136](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\图解请求重定向.png)

##### 方式一：于Servlet中手动实现

**Servlet展示**

```java
//DeprecatedServlet
public class DeprecatedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(302); //设置响应码
        resp.setHeader("location", "newServlet"); //设置请求头
    }
}
```

##### 方式二：调用resp的sendRedirect(String location)方法



##### 请求重定向的特点：

1、浏览器地址栏会发生变化

2、两次请求，也因此不共享Request中的数据

3、不能重定向到受保护的WEB-INF资源目录

4、可以重定向到域外的地址

