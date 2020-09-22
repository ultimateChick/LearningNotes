# JavaWeb

## 登录，注销

### 登录

登录之后，我们把用户的信息保存到Session域中，在任何需要判断登录状态的页面都可以通过检查Session域中user的有无来进行不同的处理。

### 注销

销毁当前连接的Session对象（invalidate），或者是Session域对象中的user对象。并**重定向**到首页。

## 表单重复提交问题

### 造成表单重复提交的三种情景

**一、提交完表单，服务器使用请求转发来进行页面跳转。**

这个时候用户按下功能键F5，就会发起最后一次的请求。造成表单重复提交的问题。

**解决方法：使用重定向来进行跳转。**



**二、用户正常提交服务器，但是由于网络延迟等原因，迟迟未收到服务器的响应，用户在提交页面就会多点几次提交，造成重复提交。**

**三、用户正常提交，服务器也正常响应，但是添加完成后用户回退浏览器再次进行提交造成表单重复提交。**

**情况二、三解决方法：使用验证码！**

![image-20200628181859457](C:\Users\q1367\Desktop\JavaWeb\书城项目总结\验证码解决重复提交问题.png)

### 使用谷歌kaptcha图片验证码

**步骤：**

1. 导入kaptcha的jar包。

2. 到web.xml中去配置生成验证码的servlet程序（jar包中有对应的servlet程序，每次调用返回一张验证码图片。

   ```xml
   <servlet>
       <servlet-name>KaptchaServlet</servlet-name>
       <servlet-class>com.google.code.kaptcha.servlet.KaptchaServlet</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>KaptchaServlet</servlet-name>
       <url-pattern>/manager/captcha</url-pattern>
   </servlet-mapping>
   ```

   

3. 在表单中使用img标签去显示验证码图片并使用它。

   ```
   <img src="manager/kaptcha.jpg"/>
   ```

4. 在服务器获取谷歌生成的验证码图片和客户端发送过来的验证码字串进行比较。

### 单击刷新验证码图片

```javascript
$(function () {
	$("#codeImg").click(
        function () {
            $(this).attr("src", "manager/kaptcha.jpg");
        }
    );
});
```

​	对验证码图片的src重新赋值为获取图片的api地址即可。

**问题：上述的方法可能发生验证码图片缓存问题**

由于每次都是从同一个地址请求图片，可能请求会被浏览器缓存下来停止取服务器请求。

**解决方案：**因为缓存的名称由资源名和请求的参数组成。因此我们给请求链接添加时间戳参数即可避免缓存问题。

```javascript
$("#codeImg").click(function () {
	$(this).attr("src", "manager/kaptcha.jpg?timestamp=" + new Date());
})
```

## 购物车模块

### 模型提取与实现方法分析

![image-20200628202856292](C:\Users\q1367\Desktop\JavaWeb\书城项目总结\购物车模型提取与实现方法分析.png)

## 订单功能全图

![image-20200629161222486](C:\Users\q1367\Desktop\JavaWeb\书城项目总结\订单功能全图.png)

## 使用Filter和ThreadLocal组合管理事务

### ThreadLocal

ThreadLocal的作用，它可以解决多线程的数据安全问题。

ThreadLocal它可以给当前线程关联一个数据（可以是普通变量可以是对象也可以是数组集合

ThreadLocal的特点：

1. ThreadLocal可以为当前线程关联一个数据。（它可以向Map一样存取数据，key可以理解为当前线程。
2. 每一个ThreadLocal对象，只能为当前线程关联一个数据，如果要为当前线程关联多个数据，就需要使用多个ThreadLocal对象实例。
3. 每个ThreadLocal对象实例定义的时候，一般都是static类型。
4. ThreadLocal中保存的数据，在线程销毁后。会由JVM虚拟机自动释放。

### 利用ThreadLocal实现连接共享，Filter实现统一提交回滚关闭管理

![image-20200701105607705](C:\Users\q1367\Desktop\JavaWeb\14.JSON、AJAX、i18n国际化\ThreadLocal管理连接.png)

**要点：**

- 一项事务可能有多个DAO组合完成功能，因此事务层需要在Service层面进行。
- 即多个DAO需要共享一个数据库连接，且在功能完成后统一进行提交关闭或者回滚关闭。（关闭前最好恢复连接的自动提交性质
- 因为每个事务最终会进行提交关闭或者回滚关闭，可以通过过滤器的作用让所有连接自动进行过滤。
- ![image-20200701110642712](C:\Users\q1367\Desktop\JavaWeb\14.JSON、AJAX、i18n国际化\过滤器拦截事务.png)

### 用TOMCAT展示统一的错误界面，对用户友好

