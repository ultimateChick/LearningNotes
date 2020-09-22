# JSP

## 一、概念引入

### 1、什么是JSP？作用是什么？

- JSP全称是Java Server Pages，即Java的服务器页面。

- JSP的主要作用是代替Servlet程序回传html页面的数据。

- 因为Servlet程序回传html页面数据是一件非常繁琐的事情，开发成本和维护成本都极高。


### 2、本质

- jsp文件本质上是一个Servlet程序。

- 当一个jsp页面被用户第一次访问的时候，Tomcat服务器会帮我们把jsp页面翻译成一个java源文件，并且对它进行翻译称为.class字节码程序。

- 查看翻译出来的java源文件会发现它继承于Apache提供的HttpJspBase，而HttpJspBase直接继承于HttpServlet。所以jsp本质上也是一个Servlet小程序。


## 二、三种语法

### 1、jsp头部的page指令

**作用：**jsp的page指令可以修改jsp页面中一些重要的属性，或者行为。

```jsp
<%@ page 
    contentType="text/html;charset=UTF-8" 
    language="java" 
    pageEncoding="utf-8"
%>
```

1、language属性：表示jsp翻译后是什么语言文件，暂时只支持java。

2、contentType属性：表示jsp返回的数据类型是什么，也是源码中response.setContentType()的传参。

3、pageEncoding属性：表示当前jsp页面文件本身的字符集。

4、import属性：跟java源代码中一样。用于导包、导类。

========================以下两个属性是给out输出流使用==============================

5、autoFlush属性：设置当out输出流缓冲区满了之后，是否自动刷新缓冲区。默认值是true。

6、buffer属性：设置out缓冲区的大小，默认是8kb。

**缓冲区溢出错误**

![image-20200619133302426](C:\Users\q1367\Desktop\JavaWeb\8.JSP\out相关的exception.png)

========================以下两个属性是给out输出流使用==============================

7、errorPage属性：设置当jsp页面运行时出错，自动跳转去的错误页面。

**注：**这个路径一般以斜杠打头，表示请求的地址为http://ip:port/工程路径/，映射到工程的web目录。

8、isErrorPage属性：设置当前jsp页面是否是错误信息页面，默认是false。如果是true，可以获取异常信息。

9、session属性：设置访问当前jsp页面，是否会创建HttpSession对象，默认是true。

10、extends属性：设置jsp翻译出来的java类默认继承于谁。

### 2、jsp中的常用脚本

#### i.声明脚本（极少使用

**格式：** <%! [声明java代码] %>

**作用：**可以给jsp翻译出来的java类定义属性和方法，甚至是静态代码块，内部类等。

![image-20200619141128990](C:\Users\q1367\Desktop\JavaWeb\8.JSP\声明脚本.png)

#### ii.表达式脚本（常用

**格式：**<%=[表达式代码]%>

**作用：**在jsp页面上输出数据。

**特点：**

1. 所有的表达式脚本都会被翻译到_jspService()方法中。
2. 表达式脚本都会被翻译成out.print()输出到页面上。
3. 由于表达式脚本翻译的内容都在_jspService()方法中，所以 _jspService()方法中的对象都可以直接使用。
4. 表达式脚本中的表达式，不能以分号结束。（分号会被out.print一起输出，造成语句提前结束。

#### iii.代码脚本

**格式：**<% [代码] %>

**代码脚本的作用：**可以在jsp页面中，编写我们自己需要的功能。

**特点：**

1. 代码脚本的内容，翻译之后原封不动的都在_jspService()方法中。

2. 代码脚本由于翻译到_jspService()中，所以方法中的所有对象都可以使用。

3. 还可以由多个代码脚本块组合完成一个大型的功能。（原封不动的特性）

   ```jsp
   <%--多个代码脚本块组合完成for语句功能--%>
   <%
       for (int j = 0; j < 10; j++) {
   %>
   
   <%
           System.out.println(j);
       }
   %>
   ```

   

4. 代码脚本还可以和表达式脚本一起组合使用，在jsp页面上输出数据。

   ```jsp
   <%--代码脚本与表达式脚本的组合--%>
   <%
       for (int j = 0; j < 10; j++) {
   %>
       <%=j%> <br>
   <%
   
       }
   %>
   ```

   ![image-20200619151333014](C:\Users\q1367\Desktop\JavaWeb\8.JSP\脚本到_jspService方法中的翻译内容.png)

### 3、JSP中的三种注释

#### i.html注释

**格式:**  <!-- [注释内容] -->

html注释会被翻译到java源代码中。以out.writer()的形式输出到客户端。

#### ii.java注释

**格式：**正常的java注释格式。

用于声明脚本和代码脚本中。java注释会被翻译到源代码中，以注释呈现。

#### iii.jsp注释

**格式：**<%-- [注释内容] --%>

jsp注释可以注释掉jsp页面中的所有代码。

## 三、jsp九大内置对象

jsp中的内置对象，是指Tomcat在翻译jsp页面成为Servlet源代码后，内部提供的就打对象，叫内置对象。

![image-20200619161236795](C:\Users\q1367\Desktop\JavaWeb\8.JSP\九大对象概览.png)

### 1、对象列表

| JSP九大内置对象 |                    |
| --------------- | ------------------ |
| request         | 请求对象           |
| response        | 响应对象           |
| pageContext     | jsp的上下文对象    |
| session         | 会话对象           |
| application     | ServletContext对象 |
| config          | ServletConfig对象  |
| out             | jsp输出流对象      |
| page            | 指向当前jsp的对象  |
| exception       | 异常对象           |

### 2、 jsp四大域对象

| 四大域对象                       | 作用范围                                                     |
| -------------------------------- | ------------------------------------------------------------ |
| pageContext（PageContextImpl类） | 当前jsp页面范围内有效                                        |
| request（HttpServletRequest类）  | 一次请求内有效                                               |
| session（HttpSession类）         | 一个会话范围内有效<br />（打开浏览器访问服务器直到关闭浏览器） |
| application（ServletContext类）  | 整个web工程范围内都有效<br />（只要web工程不停止，数据都会一直保持） |

域对象是可以像Map一样存取数据的对象。四个域对象功能一样，不一样的是他们对数据的存取范围。

##### 使用惯例

四个域在使用的时候，优先顺序是按照它们从小到大的范围顺序决定的。

每个存下来的键值对是要占用内存的，每次都在最小作用域内声明，可以最大限度的对服务器进行内存优化。

pageContext > request > session > application

##### 例子

**scope1.jsp**

```jsp
<body>
    <h1>scope.jsp页面</h1>
<%
    //往四个域中都分别保存了数据
    pageContext.setAttribute("key", "pageContext");
    session.setAttribute("key", "session");
    request.setAttribute("key", "request");
    application.setAttribute("key", "application");

%>
    pageContext域是否有值：<%=pageContext.getAttribute("key")%><br>
    session域是否有值：<%=session.getAttribute("key")%><br>
    request域是否有值：<%=request.getAttribute("key")%><br>
    application域是否有值：<%=application.getAttribute("key")%><br>
<%--    此处的请求转发使得pageContext域不起作用，离开了当前的页面对象--%>
<%
    request.getRequestDispatcher("/scope2.jsp").forward(request, response);
%>
<%--    此处的重定向使得request域不起作用，重新请求了一次，原来的请求对象失效--%>
<%
    response.setStatus(302);
    response.setHeader("Location","scope3.jsp"); //重定向是让浏览器来解析路径，斜杠代表的是不带工程路径的http://ip:port/
%>
</body>
```

**scope2.jsp**

```jsp
<body>
<h1>Scope2</h1>
pageContext域是否有值：<%=pageContext.getAttribute("key")%><br>
session域是否有值：<%=session.getAttribute("key")%><br>
request域是否有值：<%=request.getAttribute("key")%><br>
application域是否有值：<%=application.getAttribute("key")%><br>
</body>
```

**scope3.jsp**

```jsp
<body>
<h1>scope3</h1>
pageContext域是否有值：<%=pageContext.getAttribute("key")%><br>
session域是否有值：<%=session.getAttribute("key")%><br>
request域是否有值：<%=request.getAttribute("key")%><br>
application域是否有值：<%=application.getAttribute("key")%><br>
</body>
```

## 四、jsp中的out输出与response.getWriter的使用区别

response表示的是响应，我们经常用于设置返回给客户端的内容（输出）。

out也是给用户做输出使用。

### 1、代码与输出例

```jsp
<body>
<%
    out.write("out输出1<br>");
    out.write("out输出2<br>");
    response.getWriter().write("response输出1<br/>");
    response.getWriter().write("response输出2<br/>");
%>
</body>
```

![image-20200619172805671](C:\Users\q1367\Desktop\JavaWeb\8.JSP\getWriter与write的区别.png)

### 2、原理图解

![image-20200619173309396](C:\Users\q1367\Desktop\JavaWeb\8.JSP\getWriter与write的原理图解.png)

### 3、如何选择

由于jsp官方在翻译出来的源文件中默认使用out来输出，所以我们也应该使用out，以免输出的内容顺序被打乱。

### 4、out.write()与out.print()的选择

out.write()只适合用来输出字符串。

out.print()可以用来输出任何类型，因为它底层针对不同类型重载了一系列的write方法的调用，把其他类型都转换成字符串交给write了。

**结论：**在jsp页面中，可以统一使用out.print来输出数据。

## 五、jsp的常用标签

### 1、静态包含

```jsp
<body>
    头部信息<br>
    主题内容<br>
    <%--
        这就是静态包含
        file属性指定你要包含的jsp的页面路径

        地址中第一个斜杠/ :表示 http://ip:port/工程路径，映射到工程的web文件夹下
        不使用斜杠则表示在与当前页面同级文件夹下寻找

        静态包含的特点：
            1、静态包含不会翻译被包含的jsp页面。
            2、静态包含其实是把被包含的jsp页面的代码拷贝到包含的位置执行输出。
    --%>
    <%@include file="/include/footer.jsp"%>
</body>
```

**被静态包含的页面在包含它的页面中的表示：**

![image-20200619182111019](C:\Users\q1367\Desktop\JavaWeb\8.JSP\被静态包含的页面.png)

### 2、动态包含

**动态包含在jsp中的声明**

```jsp
<jsp:include page="/include/footer.jsp"></jsp:include>
```

**动态包含在翻译出来的源文件中的执行语句**

```java
org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/include/footer.jsp", out, false);
```

****

**动态包含的情况下还可以传参**

```jsp
<jsp:include page="/include/footer.jsp">
    <jsp:param name="username" value="168"/>
    <jsp:param name="password" value="lrh"/>

</jsp:include>
```

**于被包含jsp页面中通过request获取**

```jsp
<body>
    页脚信息<br>
    用户名为：<%=request.getParameter("username")%>
    密码为：<%=request.getParameter("password")%>
</body>
```

### 3、请求转发

**在jsp中使用请求转发的代码**

```jsp
<%--
    请求转发功能，page设置转发路径
--%>
<jsp:forward page="scope2.jsp"></jsp:forward>
```

![image-20200621111643360](C:\Users\q1367\Desktop\JavaWeb\8.JSP\请求转发之servlet与jsp.png)

说明：在Servlet程序中输出信息到页面是不明智的行为，因此我们可以使用request域对象保存要显示的信息，转发给相应的jsp页面来显示。

## 六、Listener监听器引入

### 1、概念

- Listener监听器是JavaWeb三大组件之一；三大组件分别是：**Servlet、Filter、Listener**。
- Lister是JavaEE的规范，就是接口。
- 监听器的作用是监听某种事物的变化，然后通过回调函数，反馈给客户去做一些相应的处理。

### 2、ServletContextListener监听器

它可以监听ServletContext对象的创建和销毁。

ServletContext对象在web工程启动的时候创建，在web工程停止的时候销毁。

监听到创建和销毁之后都会分别调用监听器的方法反馈。

其规范为：

```java
public interface ServletContextListener extends EventListener{
    /**
     ** 在ServletContext对象创建之后马上调用，做初始化
     * 这个方法在任何context和filter对象创建之前就进行调用
     * 
     */
	public void contextInitialized(ServletContextEvent sce);
    
    /**
     ** 在ServletContext对象销毁之后马上调用，做初始化
     * 这个方法在任何context和filter对象销毁之后就才进行调用
     *
     */
	public void contextDestroyed(ServletContextEvent sce);
}
```

### 3、使用步骤

1. 编写一个类去实现ServletContextListener
2. 实现其两个回调方法
3. 到web.xml中去配置监听器

**MyServletContextListenerImpl.java**

```java
public class MyServletContextListenerImpl implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletContext对象稍后就创建");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext对象已经被销毁了");
    }
}
```

**web.xml**

```jsp
<!--
    配置监听器
-->
<listener>
    <listener-class>com.atguigu.listener.MyServletContextListenerImpl</listener-class>
</listener>
```