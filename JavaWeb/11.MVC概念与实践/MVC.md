# MVC

## 概念

MVC全称：Model、View、Controller

MVC最早出现在JavaEE三层中的web层，它可以有效地指导Web层的代码如何有效分离，单独工作。

View视图：只负责数据和界面的显示，不接受任何与显示数据无关的代码，便于程序员和美术的分工合作——JSP/HTML。

Contorller控制器：只负责接收请求，调用业务层的代码处理请求，然后派发页面，是一个“调度者”的角色——Servlet。

转到某个页面。或者是重定向到某个页面。

Model模型：将与业务逻辑相关的数据封装为具体的JavaBean类，其中不掺杂任何与数据处理相关的代码——JavaBean/domain/entity/pojo。

**MVC是一种思想**

MVC的理念是将软件代码拆分成为组件，单独开发，组合使用（**解耦合**

![image-20200623140045513](C:\Users\q1367\Desktop\JavaWeb\11.MVC概念与实践\MVC图解.png)

## 前后台的简要认识

![image-20200623172609880](C:\Users\q1367\Desktop\JavaWeb\11.MVC概念与实践\前后台认识.png)

## 表单的重复提交

当用户提交完请求，浏览器会记录下该请求的最后一次提交全部信息。一旦刷新，发起的请求含有刚才提交的表单信息。

解决方案：

所以在处理表单信息之后最好使用重定向request.setRedirect来转到相应的页面，即创建一个新的请求。