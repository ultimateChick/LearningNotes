# Cookie与Session

## Cookie

### 引入

1. Cookie意即饼干屑，是服务器通知客户端保存键值对的一种技术。
2. 客户端有了Cookie后，每次请求都发送给服务器。
3. 每个Cookie的大小不能超过4kb。

​	

### 如何创建Cookie

```java
    protected void createCookie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie cookie = new Cookie("key1","value1");
        //解决响应中文乱码问题
        resp.setContentType("text/html; charset=UTF-8");
        resp.addCookie(cookie);
        resp.getWriter().write("cookie创建成功");
    }
```

### 获取Cookie

```java
    public static Cookie findCookie(String name, Cookie[] cookies){
        if (name == null || cookies == null || cookies.length ==0){
            return null;
        }

        for (Cookie c: cookies){
            if (name.equals(c.getName())){
                return c;
            }
        }
        return null;
    }
```

```java
    protected void getCookie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        Cookie cookie = CookieUtils.findCookie("key1", cookies);
        System.out.println(cookie);
    }
```

### 修改Cookie

![image-20200627215745671](C:\Users\q1367\Desktop\JavaWeb\12.Cookie与Session\修改Cookie策略.png)

![image-20200627220225419](C:\Users\q1367\Desktop\JavaWeb\12.Cookie与Session\setValue说明.png)

### Cookie生命控制

Cookie的生命控制指的是如何管理Cookie什么时候被销毁、删除。

**setMaxAge()**

![image-20200627220854178](C:\Users\q1367\Desktop\JavaWeb\12.Cookie与Session\maxAge.png)

### Cookie有效路径Path

Cookie的Path属性可以有效地过滤哪些Cookie可以发送服务器，哪些不发。

Path属性是通过请求的地址来进行有效的过滤。

### Cookie实现免用户名登录

**![image-20200627232608103](C:\Users\q1367\AppData\Roaming\Typora\typora-user-images\image-20200627232608103.png)**

## Session

### 什么是Session会话

1. Session就一个接口 HttpSession
2. Session就是会话，它是用来维护一个客户端和服务器之间关联的技术。
3. 每个客户端都有自己的一个Session会话。
4. Session中，我们经常用来保存用户登录之后的信息。

### 如何创建Session和获取（id号，是否为新

如何创建和获取Session。他们的API是一样的。

request.getSession()

​	第一次调用是：创建Session会话

​	之后调用：都是获取前面创建好的Session会话对象。

isNew(); 判断到底是不是刚创建出来的（新的

​	true 表示刚创建    request.getSession().isNew()

​	false 表示获取**之前**创建

每个会话都有一个身份证号码ID

getId得到Session的会话id值。

### Session内容的存取

request.getSession.setAttribute(key,value);

Object value = request.getSession.getAttribute(key);

### Session生命周期控制

#### 超时的概念

指的是当前时间距离最后一次使用session对象（两次请求间）所使用的时间需要超过设定的超时时间才算超时。

时长设置为负数表示永不超时（极少使用

**以秒为单位**

public void setMaxInactiveInterval(int interval)

设置Session超时时间，超过指定的时长，Session就会被销毁。

public void getMaxInactiveInterval(int interval)

获取Session超时时间，超过指定的时长，Session就会被销毁。

**默认超时与配置**

---30分钟---

tomcat服务器的配置文件web.xml中有默认的配置，他表示配置了当前tomcat服务器下所有的session超时配置默认时长为30分钟。

```xml
<session-config>
	<session-timeout>30</session-timeout>
</session-config>
```

此选项可以单独在独立的web工程的web.xml当中配置，表示此工程下的所有session的默认超时时长。

如果需要单独配置特定session的超时时长，则需要利用api提供的方法。setMaxInactiveInterval(int interval)

**使会话马上超时**

public void invalidate()

使此会话无效，然后取消对任何绑定到它的对象的绑定。

### Session的生命周期

#### 创建

第一次使用的时候创建：

​	任何操作需要用到session，而我们发送的请求中不包含JSESSIONID或者包含的id匹配不到session对象时，就创建一个新的。

#### 销毁

超时、手动调用invalid方法

### 浏览器与Session之间关联的技术内幕

![image-20200628161935617](C:\Users\q1367\Desktop\JavaWeb\12.Cookie与Session\浏览器与服务器Session之间的关联内幕.png)

- 浏览器初次与服务器建立连接，服务器会为浏览器创建一个Session对象，维护客户端和服务器之间的关联。
- 在响应的时候，服务器会把新创建出来的Session对象的ID，通过Cookie的形式告诉浏览器客户端。
- 之后客户端在发送任何请求的时候，由于会把本地的Cookie对象一并发送给服务器，服务器会从Cookie中找到对应的SessionId，从而寻访到该客户端所对应的Session对象，这也是**为无状态的HTTP协议带入状态**的做法。

#### 为什么Session默认关闭浏览器时销毁

因为set-Cookie的expire默认是Session（意即关闭浏览器时结束生命周期

#### 引申：session活化与钝化

钝化：当服务器正常关闭时，还存活着的session（在设置时间内没有销毁）会随着服务器的关闭被以文件（"SESSION.ser"）的形式存储在tomcat的work目录下，这个过程叫做Session的钝化

活化：当服务器正常开启时，服务器会去work文件夹下找到SESSION.ser文件，并从中恢复之前保存起来的Session对象，这个过程叫做Session的活化

##### 注意事项：

想要随着Session被钝化、活化的对象它的类必须实现Serializable接口。

SESSION.ser只会随着服务器的下一次正常启动而消失

一个ser文件保存着多个session对象

![这里写图片描述](C:\Users\q1367\Desktop\JavaWeb\12.Cookie与Session\Session活化与钝化.png)