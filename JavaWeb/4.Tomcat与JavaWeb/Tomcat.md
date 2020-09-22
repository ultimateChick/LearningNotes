# Tomcat

## 一、JavaWeb引入

### 1、何为JavaWeb？

所有通过Java语言编写可以通过浏览器访问的程序的总称。

它是基于请求和响应来开发的。

#### 什么是请求？

请求是指客户端给服务器发送数据，叫请求Request。

#### 什么是响应？

响应是指服务器给客户端回传数据，叫响应Response。

#### 请求和响应的关系

请求和响应是成对出现的，有请求就有响应。

### 2、Web资源的分类

web资源按实现的技术和呈现的效果的不同，又分为静态资源和动态资源两种。

**静态资源：**html、css、js、txt、jpg、mp4等

**动态资源：**jsp页面、servlet程序

### 3、常用的Web服务器

**Tomcat：**由Apache组织提供的一种Web服务器，提供对jsp和Servlet的支持。它是一种轻量级的JavaWeb容器（服务器），也是应用最广的JavaWeb服务器（免费）。

**JBoss：**是一个遵从JavaEE规范的、开放源代码的，纯Java的EJB服务器，支持所有的JavaEE规范。

**GlassFish：**由Oracle公司开发的一款JavaWeb服务器，商业级

![image-20200616124008671](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\常用的Java服务器.png)

### 4、Tomcat服务器和Servlet版本对应关系

![image-20200616124056492](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\版本对应关系.png)

​	Tips：Servlet从3.0之后是注解版本的，在之前是xml配置的

### 5、Tomcat的使用

**a) 安装**

解压到特定目录然后配置环境变量即可。

**b)目录介绍**

![image-20200616125749137](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\tomcat目录介绍.png)

钝化：即序列化，把Session对象写到磁盘上。

**c)如何启动Tomcat服务器**

目录下bin目录的startup.bat文件。

或者是命令行：

测试：本地8080端口

**d)如何修改Tomcat默认端口**

找到Tomcat目录下的conf目录，找到server.xml配置文件。

![image-20200616133357006](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\tomcat配置文件.png)

把port属性修改为你需要的端口号。（1-65535）重启生效。

**e)如何部署web工程到Tomcat中**

1、直接把web工程的目录复制到webapp目录下一文件夹即可。

2、找到Tomcat下的conf目录下的\Catalina\localhost\下，创建配置文件，可以自定义工程访问路径，并定义工程目录位置。

```xml
<!-- 
Context表示一个工程上下文
path表示工程访问路径 :/abc
docBase表示你的工程目录在哪里
-->
<Context path="/abc" docBase="E:\book"></Context>
```

**f)比较本地与网络访问页面的方式**

![image-20200616150813567](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\本地与网络比较.png)

**g)ROOT工程的访问，以及默认index.html页面的访问**

没有指定工程名，默认访问ROOT工程。

如果没有指定资源名，默认访问index.html页面

### 6、IDEA整合Tomcat服务器

在Setting中的ApplicationServer添加Tomcat支持，便可创建Java Enterprise。

选择以Tomcat为服务器的带webapp支持的模式。

##### 目录解释

![image-20200616153358102](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\webapp目录解释.png)

##### 依赖配置

方式一：在lib文件夹下加入要使用的jar包，Add As Libraries，添加为当前web应用的依赖。

方式二：在Project Structure中，先在Libraries中引入lib文件夹，添加为当前webapp依赖，然后在Artifacts中fix即可。

##### 启动服务器

1、建议修改工程对应的tomcat实例名称，以防混淆

![image-20200616154641107](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\tomcat实例修改.png)

2、Deployment说明

![image-20200616155132729](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\一个实例对应多个工程.png)

3、服务器默认启动的工程路径配置

![image-20200616155324365](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\配置服务器默认行为.png)

4、重启选单

![image-20200616162319864](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\重启选单.png)

5、选择有修改时进行热部署，作如下修改：

![image-20200616163254595](C:\Users\q1367\Desktop\JavaWeb\4.Tomcat与JavaWeb\修改资源更新行为.png)