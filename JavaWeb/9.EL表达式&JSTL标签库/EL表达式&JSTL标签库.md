# EL表达式&JSTL标签库

## EL表达式

### 一、引入

#### 1、什么是EL表达式，EL表达式的作用？

EL表达式的全称：Expression Language，即表达式语言。

作用：EL表达式主要是代替jsp页面中的表达式脚本在jsp页面中进行数据输出。

EL表达式在输出数据的时候，要比jsp的表达式脚本简洁很多。

#### 2、简洁性表现

**原生和EL表达式对空值的处理**

```jsp
<body>
    <%
        request.setAttribute("key", "值");
    %>
    表达式脚本输出：
    <%=request.getAttribute("key1")%><br>
    EL表达式输出：
    ${key1}
</body>
```

**输出**

![image-20200621123741225](C:\Users\q1367\Desktop\JavaWeb\9.EL表达式&JSTL标签库\控制输出.png)

**改良**

```jsp
<%=request.getAttribute("key1")==null?"":request.getAttribute("key1")%><br>
```

从这里可以看出EL表达式的简洁之处。

### 二、特性

#### 1、EL表达是搜索域数据的顺序

EL表达是主要在jsp页面中输出域对象中的数据。

```jsp
<body>
<%
    pageContext.setAttribute("key","pageContext");
    request.setAttribute("key","request");
    session.setAttribute("key","session");
    application.setAttribute("key","application");
%>
<%--从作用域小到大的顺序输出，这里输出pageContext--%>
EL表达式输出域数据：${key} 

</body>
```

#### 2、EL表达式输出复杂的Bean对象

![image-20200621135317760](C:\Users\q1367\Desktop\JavaWeb\9.EL表达式&JSTL标签库\复杂Bean对象的输出.png)

​	EL表达式对属性的获取，会从对象的get方法中去做相应的匹配，即使对象并不包含那个属性，只是有命名为该属性的get方法。

#### 3、关系运算

![image-20200621135746214](C:\Users\q1367\Desktop\JavaWeb\9.EL表达式&JSTL标签库\关系运算表.png)

#### 4、逻辑运算

![image-20200621141012453](C:\Users\q1367\Desktop\JavaWeb\9.EL表达式&JSTL标签库\逻辑运算.png)

#### 5、算术运算

![image-20200621141146262](C:\Users\q1367\Desktop\JavaWeb\9.EL表达式&JSTL标签库\算术运算.png)

#### 6、empty运算

​	empty运算可以判断一个数据是否为空，如果为空，则输出true；不为空输出false。

​	以下几种情况为空：

1. 值为null的时候为空
2. 值为空串时候为空
3. 值是Object类型数组，长度为零的时候
4. list集合，元素个数为零
5. map集合，元素个数为零

#### 7、"."点运算 和 []中括号运算符

.点运算，可以输出Bean对象中某个属性的值。

[]中括号运算，可以输出有序集合中某个元素的值。

并且[]还可以输出map集合中key里含有**特殊字符**的key的值。

```jsp
<body>
<%
    Map<String, Object> map = new HashMap<>();
    map.put("a.a.a","aaaValue");
    map.put("b-b-b","bbbValue");
    map.put("c+c+c","cccValue");
%>
    ${map.a.a.a} <%-- 输出空值 --%>
    ${map["a.a.a"]} <%-- 输出aaaValue --%>
</body>
```

### 三、EL表达式中11个隐含对象

| 变量             | 类型                 | 作用                                                 |
| ---------------- | -------------------- | ---------------------------------------------------- |
| pageContext      | PageContextImpl      | 它可以获取jsp中的九大内置对象                        |
| pageScope        | Map<String,Object>   | 它可以获取PageContext域中的数据                      |
| requestScope     | Map<String,Object>   | 它可以获取Request域中的数据                          |
| sessionScope     | Map<String,Object>   | 它可以获取Session域中的数据                          |
| applicationScope | Map<String,Object>   | 它可以获取Application域中的数据                      |
| param            | Map<String,String>   | 它可以获取请求参数的值                               |
| paramValue       | Map<String,String[]> | 它可以获取请求参数的值，获取多个值的时候使用         |
| header           | Map<String,String>   | 它可以获取请求头的信息                               |
| headerValue      | Map<String,String[]> | 它可以获取请求头的信息，用于多个值的情况             |
| cookie           | Map<String,Cookie>   | 它可以获取当前请求的Cookie信息                       |
| initParam        | Map<String,String>   | 它可以获取在web.xml中配置的<context-param>上下文参数 |

## JSTL表达式

### 一、引入

#### 1、什么是JSTL标签库，它的作用是什么？

- JSTL标签库全称是指 JSP Standard Tag Library JSP标准标签库。是一个不断完善的开放源代码的JSP标签库。
- EL表达式主要是为了替换jsp中的表达式脚本，而标签库则是为了替换代码脚本。这样使得整个jsp页面变得更加简洁。

### 二、内容

#### 1、JSTL由五个不同功能的标签库组成。

![image-20200621165946073](C:\Users\q1367\Desktop\JavaWeb\9.EL表达式&JSTL标签库\5大核心库.png)

#### 2、在jsp标签库中使用taglib指令引入标签库

![image-20200621170623028](C:\Users\q1367\Desktop\JavaWeb\9.EL表达式&JSTL标签库\标签库引入.png)

#### 3、标签库具体使用

1. **<c:set />**

   它可以用来往域中保存数据。

   ```jsp
   <c:set scope="page/request/session/application" var="key" value="value"/>
   ```

   

2. **<c:if />**

   ```jsp
   <%--
       if标签用来做判断，替代的是java代码中的if()
       test属性表示判断的条件(使用EL表达式输出)
       不能写成if-else。
   --%>
   <c:if test="${12==12}">
   	<h1>12等于12</h1>
   </c:if>
   ```

   

3. **<c:choose > <c:when > <c:otherwise >**

   作用：多路判断。跟switch功能很像。

   <c:when >一定要被<c:choose > 包含。

4. **<c:forEach />**

   作用：遍历输出使用。

   1. 遍历输出1-10

      ```jsp
      <body>
          <%--1.遍历1到10，输出
              begin属性设置开始的索引
              end设置结束的索引
              var属性表示遍历的变量（也是当前正在遍历到的数据
              for (int i = 1; i < 10; i++)
          --%>
          <c:forEach begin="1" end="10" var="i">
              ${i}
          </c:forEach>
      </body>
      ```

      

   2. 遍历Object数组

      ```jsp
      <%--2.遍历Object数组
          for(Object o: arr)
          items表示遍历的数据源
          var表示当前遍历到的数据
      --%>
      <%
          request.setAttribute("arr", new String[]{"12", "we", "34e"});
      %>
      <c:forEach items="${requestScope.arr}" var="o">
          ${ o }<br>
      </c:forEach>
      ```

      

   3. 遍历map数组

      ```jsp
      <%--3.遍历map数组
      --%>
      <%
          Map<String, Object> map = new HashMap<>();
          map.put("key1", "value1");
          map.put("key2", "value2");
          map.put("key3", "value3");
          for(Map.Entry<String ,Object> entry: map.entrySet()){
              entry.getKey();
              entry.getValue();
          }
          request.setAttribute("map",map);
      %>
      <c:forEach items="${requestScope.map}" var="entry">
      ${entry.key}
      </c:forEach>
      ```