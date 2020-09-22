# JavaScript

## 1. 引入

JS是弱类型的语言，用于编写、强化网页的动作

### 特点：

1.交互性：它可以做的就是信息的动态交互

2.安全性：不允许直接访问本地硬盘

3.跨平台性：只要是可以解释JS的浏览器都可以执行，和平台无关

## 2. JS与HTML的结合方式

### 2.1. 第一种方式

只要在head标签中，或者在body标签中，使用script标签就可以来书写js代码

### 2.2. 第二种方式

```
·使用script引入外部的js文件来执行，src指向引入的js文件路径
·script标签可以用来定义js代码，也可以用来引入js文件
·但是要注意，两者二选一使用，不能同时使用功能(使用了src时内部封装的代码会被忽略)
```

## 3. 变量

### 3.1. 变量类型

数值类型：	number

字符串类型： string

对象类型：	object

布尔类型：	boolean

函数类型：	function

### 3.2. JS里特殊的值

undefined：	未定义，所有js变量未赋予初始值的时候，默认值都是undefined

null：				空值

NAN：			   全称是 Not a Number，表示非数值

### 3.3. 定义变量格式

var 变量名 = 变量值；

var 变量名；

## 4. 关系运算

等于：	==		等于是简单的做字面值的比较

全等于：===	

## 5. 逻辑运算

在JavaScript语言中，所有的变量，都可以作为一个boolean类型的变量去使用

0、null、undefined、“”（空串） 都认为是false；

#### 1.&&运算

两种情况：

1. 当表达式全为真的时候，返回最后一个表达式的值
2. 当表达式中，有一个为假的时候，返回第一个为假的表达式的值

#### 2.||运算

两种情况：

	1. 当表达式全为假时，返回最后一个表达式的值
 	2. 只要有一个表达是为真，就会把第一个为真的表达式的值返回

## 6. 数组

### 6.1. 定义方式

格式：

var 数组名 = []; //空数组

var 数组名 = [1, "abc", true]; //定义数组的同时赋予元素，且元素的类型丰富多彩

### 6.2. 动态性

只要我们通过数组下标赋值（单纯的读操作不行），就会动态扩容到至少满足下标的长度

## 7. 函数

### 7.1. 定义方式

1. **可以使用function关键字来定义函数：**

   #### 无返回值函数

   function 函数名（形参列表）{

   ​	方法体；

   }

   函数的形参只需要指明形参名称，不用指定类型。

   eg:  

   function func(a, b){

   ​	alert("有参函数func被调用了  a=>" + a + ", b=>"+b);

   }

   #### 有返回值函数

   只需要在函数体内直接使用return语句，返回返回值即可。

2. **函数变量形式：**

   格式如下：

   var 函数名 = function(形参列表){ 函数体 }

### 7.2. 特色

1. JS中的函数不允许重载，只能覆盖掉上一次的定义

2. 即是传参不符合定义的个数，而依然可以传参，只是无效

   **原因：** 

   js中的函数提供了arguments的隐性参数。

   在function函数中不需要定义，但却可以直接用来获取所有参数的变量。好比java中的可变长参数。

   js中的隐性参数的操作也同可变长参数相似，操作类似数组。

   eg：

   ```javascript
   function sum(){
       var result = 0;
       for (var i = 0; i < arguments.length;i++){
           result = result + arguments[i];
       }
       alert(result);
       return result;
   }
   ```

## 8. 自定义对象

### 8.1. Object形式的自定义对象

eg:

```javascript
var obj = new Object();
obj.field1 = 123;
obj.func1 = function () {
    alert("niayigoji");
    alert("属性：" + this.field1);
};

obj.func1();
```

### 8.2. {}形式的自定义对象

eg:

```javascript
var obj1 = {
    field1: "niayigoji",
    field2: "???",
    
    func1: function () {
        alert("简单");
    }
};
alert(typeof obj1);
obj1.func1();
```

## 9. 事件

电脑输入设备与页面进行交互的响应，我们称之为事件。

#### 常用的事件：

· onload 加载完成事件： 页面加载完成之后，常用于js初始化操作。

· onclick 单击事件：常用于按钮的点击相应操作。

· onblur 失去焦点事件：常用于输入框失去焦点后验证其输入内容是否合法。

· onchange内容发生改变事件：常用于下拉框和输入框内容发生改变后操作

· onsubmit表单提交事件：常用于表单提交前，验证所有表单项是否合法



### 事件的注册：

**什么是事件的注册（绑定）？**

其实就是告诉浏览器，当事件响应后要执行哪些操作代码，叫事件注册或事件绑定。

#### 静态注册事件：

​	通过html标签的事件属性直接赋予事件响应后的代码，这种方式我们叫静态注册。

```javascript
    <script type="text/javascript">
        function onloadFun() {
            alert("静态注册onload事件，所有代码");
        }
    </script>
</head>
<!--静态注册方法-->
<!--
    onload事件是浏览器解析完页面之后，就会自动触发的事件
-->
<body onload="onloadFun()">

</body>
```

#### 动态注册事件：

​	是指先通过js代码，得到标签的dom对象，然后再通过dom对象.事件名=function(){}这种形式赋予事件响应后的代码，叫动态注册。

​	动态注册基本步骤：

​		1、获取标签对象

​		2、标签对象.事件名 = function(){};

```javascript
//onload事件的动态注册，是固定写法
window.onload = function () {
    alert("动态注册的onload事件");
}
```

##### onclick、onblur、onload、onchange都是一样的绑定套路，例子如下：

```javascript
    <script type="text/javascript">
        function onblurFunc() {
            //console是控制台对象，是由JavaScript提供，用于向浏览器的控制台打印输出，常用于测试使用
            //log()是打印的方法
            console.log("失焦");
        }

        //动态注册onblur事件
        window.onload = function () {
            var passwordObj = document.getElementById("pwInput");
            passwordObj.onblur = function () {
                console.log("失焦");
            };
        }
    </script>
</head>
<body>
用户名:<input type="text" onblur="onblurFunc()"><br>
密码：<input type="text" id="pwInput"><br>

</body>
```

##### onsubmit有不同，根据返回值可以影响提交与否，例子如下：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript">
        function noSubmit() {
            alert("阻止提交");
            return false;
        }
    </script>
</head>
<body>
<form action="onblur.html" method="get" onsubmit="return noSubmit()">
    <input type="submit">
</form>
</body>
</html>
```

**特别的，onSubmit方法必须持有return不能省略，不然就是onsubmit="false"而非"return false"，无法阻止提交。**