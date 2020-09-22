# HTTP协议

## 一、引入

### 什么是协议？

协议是指双方或者多方，相互约定好都必须遵守的规则。

所谓的HTTP协议，就是指客户端和服务器之间通信时，发送的数据约定好的格式，需要遵守的规则等。

HTTP协议发送的数据叫做报文。

## 二、格式说明

### 请求的HTTP协议格式

客户端给服务器发送数据叫请求，服务器给客户端回传数据叫响应。

请求又分为get与post两种请求。

#### i. GET请求

1、先有请求行

​	(1)请求的方式					GET

​	(2)请求的资源路径[+?+请求参数]

​	(3)请求的协议和版本号	HTTP/1.1			

2、再有请求头

​	key:value组成		不同的键值对表示不同的含义

![image-20200617101843226](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\HTTP GET请求图解.png)

#### ii. POST请求

1、请求行

​	(1)请求的方式					GET

​	(2)请求的资源路径[+?+请求参数]

​	(3)请求的协议和版本号	HTTP/1.1		

2、请求头

​	key : value组成		不同的键值对表示不同的含义

**-------------------------------空行---------------------------------**

3、请求体

​	包含发送给服务器的数据

![image-20200617103234881](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\HTTP POST请求图解.png)

#### iii. 常用请求头的说明

![image-20200617103546375](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\常用的请求头.png)

#### iv. 区分GET与POST请求

**哪些是GET请求？**

​	1、form标签 method='get'

​	2、link引入css文件

​	3、script引入js文件

​	4、a标签

​	5、iframe标签

​	6、img标签

​	7、在浏览器地址栏敲入地址按回车

**哪些是POST请求？**

​	1、form标签 method='POST'

### 响应的HTTP协议格式

1、响应行

​	(1) 相应的协议和版本号

​	(2) 响应状态码

​	(3) 响应状态描述符

2、响应头

​	(1)key : value			不同的响应头有其不同含义

**-------------------------------空行---------------------------------**

3、响应体

​	回传给客户端的数据

![image-20200617110524813](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\HTTP响应图解.png)

**常见的响应码说明：**

200		表示请求成功

302		表示请求重定向

404		表示请求服务器已经收到了，但是你要的数据不存在

500		表示服务器已经收到请求，但是服务器内部错误

### MIME格式说明

MIME是HTTP协议中的数据类型。

MIME的英文全称是“Multipurpose Internet Mail Extensions” 多功能 Internet邮件扩充服务。MIME类型的格式是“大类型/小类型”，并与某一种文件的扩展名相对应。

常见的MIME类型：

![image-20200617112228290](C:\Users\q1367\Desktop\JavaWeb\6.HTTP\MIME类型对照表.png)