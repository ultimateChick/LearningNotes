# JQuery

## 一、介绍

### 1. JQuery是什么？

JQuery意即JavaScript与Query，它是辅助JavaScript开发的js类库

### 2. 核心思想

**Write Less, Do More!** 所以它实现了对很多的浏览器兼容性问题的解决。

## 二、HelloWorld

```html
<script type="text/javascript" src="../script/jquery-1.7.2.js"></script>
<script type="text/javascript">
   // window.onload = function () {
   //     var btnObj = document.getElementById("btnId");
   //     // alert(btnObj);//[object HTMLButtonElement]   ====>>>  dom对象
   //     btnObj.onclick = function () {
   //        alert("js 原生的单击事件");
   //     }
   // }

   $(function () { // 表示页面加载完成 之后，相当 window.onload = function () {}
      var $btnObj = $("#btnId"); // JQuery对象习惯上用$开头,表示按id查询标签对象
      $btnObj.click(function () { // 绑定单击事件
         alert("jQuery 的单击事件");
      });
   });

</script>
```

**QA：**

$到底是什么？  **$是一个函数**

怎么为按钮添加点击响应函数的？

**1、使用JQuery查询到标签对象**

**2、使用标签对象.click(function(){})**

## 三、核心函数介绍

$是JQuery的核心函数：

**1、传入参数为[函数]时：**

表示页面加载完成之后。相当于window.onload = function(){}

**2、传入参数为[HTML 字符串]时：**

会为我们创建这个html标签对象。并自动添加为body子节点

**3、传入参数为[选择器字符串]时：**

$("#id属性值"): id选择器，根据id查询标签对象

$(".class属性值"):类型选择器，可以根据class属性查询标签对象

$("标签名"):标签名选择器，根据指定的标签名查询标签对象

**4、传入参数为[DOM对象]时：**

会把这个dom对象转换为JQuery对象

**tips：**

通过JQuery提供的API创建的对象，是JQuery对象

通过JQuery包装的Dom对象，也是JQuery对象

通过JQuery提供的API查询到的对象，也是JQuery对象

## 四、认识JQuery对象

JQuery对象本质是：**DOM对象的数组 + JQuery提供的一系列功能函数**



#### JQuery和DOM对象的使用区别

JQuery对象不能使用DOM对象的属性和方法，反之亦然。

#### DOM对象与JQuery对象的互转

**1、dom对象转化为JQuery对象**

1、先有DOM对象

2、$(DOM对象)包装得到JQuery对象

**2、JQuery对象转为dom对象**

1、先有JQuery对象

2、JQuery对象[下标]取出相应的DOM对象

![image-20200612150502260](C:\Users\q1367\Desktop\JavaWeb\2.JQuery\JQuery与DOM对象互转.png)

## 五、JQuery选择器（重点）

## 原则：结果的顺序和代码的顺序一致

### 5.1. 基本选择器

#### #id

根据元素id属性值查找对象

#### element（标签选择器）

选择文档中所有指定标签的对象

#### .class

根据给定的类匹配元素

#### *(全选选择器)



#### $("selector1,selector2,...")组合选择器



### 5.2. 层级选择器

#### ancestor descendant

概述：在给定的祖先元素下匹配所有的**后代**元素
参数：

​	ancestor：任何有效选择器

​	descendant：用以匹配元素的选择器，并且它是一个选择器的后代元素

#### parent > child

概述：在给定的父元素下匹配所有的**子**元素

#### prev + next

概述：匹配所有**紧接**在prev元素后的**一个**next元素

#### prev ~ siblings

概述：匹配prev元素之后的所有**siblings**元素

### 5.3. 过滤选择器

#### 基本过滤器

**:first**

获取匹配的第一个元素

eg: $("li :first")

**:last**

获取匹配的最后一个元素

**:not()**

查找所有未选中的input元素，括号内可接属性过滤器

**:even**

匹配所有索引值为偶数的元素（查找第1/3/5/...行）

**:odd**

匹配所有索引值为奇数的元素(查找第2/4/6/...行)

**:eq(index: number)**

匹配一个给定索引值的元素

**:gt(index: number)**

匹配所有大于给定索引值的元素

**:lt(index: number)**

匹配所有小于给定索引值的元素

**:header**

匹配如h1，h2...之类的标题元素

**:animated**

匹配所有正在执行动画效果的元素

#### 内容过滤器

**:contains(text)**

匹配包含给定文本的元素

**:empty**

匹配所有不包含子元素或者文本的空元素

**:parent**

匹配所有包含子元素或者文本的元素

**:has(selector)**

匹配含有选择器所匹配的元素的元素

#### 属性过滤器

**[attribute]**

匹配包含给定属性的元素。注意，在JQuery 1.3中，前导的@符号已经被废除！

**[attribute=value]**

匹配给定的属性是某个特定值的元素

**[attribute!=value]**

匹配所有不含有指定的属性，或者属性不等于特定值的元素。

选择器等价于 :not([attribute=value])，要匹配必须含有特定属性，但属性值不等于特定值的情况可以通过[attribute]:not([attrbute=value])

**[attribute^=value]**

匹配给定的属性是以某些值开始的元素

**[attribute$=value]**

匹配给定的属性是以某些值结尾的元素

**[attribute*=value]**

匹配给定的属性是已包含某些值的元素

**[ selector1 ] [selectorN]（不含空格！）**

复合选择器，需要同时满足多个条件时使用

#### 表单过滤器

**:input**

匹配所有的input元素

**:text**

匹配所有的单行文本框(type = "text")

**:password**

匹配所有密码框

**...所有表单中的input支持的type**

#### 表单对象属性

**:enabled    :disable**

返回所有可用/不可用的表单项

**:checked**

匹配所有被选中的可选中元素（复选框、单选框等，不包括select中的option)

**:selected**

匹配所有选中的option元素

#### 表单项方法

**.val()**

可以操作表单项的value属性值

## 六、JQuery元素筛选

#### filter(expr|obj|ele|fn)

筛选出与制定表达式匹配的元素集合。

这个方法用于缩小匹配的范围，用逗号分割多个表达式

```html
<script type="text/javascript" src="JQuery..."></script>
<script type="text/javascript">			
    //(4)filter()在div中选择索引为偶数的,改其背景色为#bfa
    $("#btn4").click(function(){
    //filter()  过滤   传入的是选择器字符串
    $("div").filter(":even").css("background-color","#bfa");
    });
</script>
```

**is(expr|obj|ele|fn)**

判断指定集合是否匹配给定的选择器，只要有一个匹配就返回true

**not(expr|obj|ele|fn)**

删除与指定表达式匹配的元素

**children([expr])**

取得一个包含匹配的元素集合中每一个元素的所有子元素的符合expr筛选的元素集合

**find(exp)**

等同于 "ancestor descendant"

**next([expr])**

返回当前元素的下一个兄弟元素，等同于 prev+next

**nextAll()**

返回当前元素后面所有的兄弟元素，等同于prev~next，是为左开区间

**nextUntil([expr])**

返回当前元素到指定匹配的元素位置之间的所有元素，是为左右开区间

**parent([expr])**

用来筛选的表达式

**prev([expr])**

取得一个包含匹配的元素集合中每一个元素紧邻的前一个同辈元素的元素集合

**prevAll([expr])**

查找当前元素之前所有的同辈元素（支持表达式过滤

**prevUntil([expr])**

同理于nextUntil，只不过是从后往前

**siblings([expr])**

返回所有兄弟元素

**add()**

把add匹配的选择器的元素添加到当前JQuery对象中

## 七、属性操作

### 7.1. JQuery的属性操作

#### html([value])

它可以设置和获取起始标签和结束标签中的内容，和DOM对象中的innerHTML方法一致

#### text([value])

他可以设置和获取起始标签和结束标签中的文本。和DOM对象中的innerText一样

#### val([value])

他可以设置和获取表单项的value属性值，和DOM对象中的val()方法一致

val()还可以同时设置多个选中输入框的选中状态。比如radio、select、checkbox等等。

用[]把要选中的选项对应的val属性值装入，用逗号依个隔开。

JQuery会自动匹配具体的选中选项和调用方中的对象。

**TIPS: 统一的，传参数是设置，不传参数是获取**

#### attr()

可以设置和获取属性值，不推荐操作checked、readOnly、selected、disabled等等

attr()还可以操作非标准的，比如自定义的属性：abc、bbj…

#### prop()

可以设置和获取属性值，只推荐操作checked、readOnly、selected、disabled等等

## 八、DOM的增删改

### 内部插入：

appendTo()                        a.appendTo(b)              把a插入到b子元素末尾，成为最后一个子元素

prependTo()                       a.prependTo(b)            把a插到b所有子元素前面，成为第一个子元素

### 外部插入：

insertAfter()                        a.insertAfter(b)            得到ba

insertBefore()                     a.insertBefore(b)         得到ab

### 替换：

replaceWith()                      a.replaceWith(b)          用b替换a  (把所有的a替换成一个b)

replaceAll()                          a.replaceAll(b)          用一个b替换对应一个a（有多少a就有多少b）

### 删除：

remove()                              a.remove()                    删除a标签

empty()                                a.empty()                      清空标签内封装的数据，但不影响标签本身的属性

## 九、CSS样式操作

**addClass()** 		向被选元素添加一个或多个类，以实现CSS中对类写好的样式

**removeClass()**		删除样式

**toggleClass()**		有就删除，没有就添加样式

**offset()**		获取和设置元素的坐标

## 十、JQuery动画

### 基本动画

**show()**		将隐藏的元素显示

**hide()**		将可见的元素隐藏

**toggle()**		可见就隐藏，不可见就显示



### 淡入淡出动画

**fadeIn()**		淡入(慢慢可见)

**fadeOut()**		淡出(慢慢消失)

**fadeTo(持续时间，透明度，回调函数)**		

​		在指定时长内，慢慢地将透明度修改到指定的值。(0-1多种透明度)

**fadeToggle()**		淡入淡出切换

​	以上动画方法都可以添加参数：

​		1、第一个参数是动画执行的时长，以毫秒为单位

​		2、第二个参数是回调函数，在动画执行完成时调用。

## 十一、事件操作

### 事件方法使用

**$(function(){});	和	window.onload = function(){}	的区别？**

1、他们分别是在什么时候触发？

答：JQuery的页面加载完成之后，是浏览器的内核解析完页面的标签，创建好DOM对象之后就执行。

```javascript
//以下两个函数是一致的
$(function(){})
$(document).ready(function(){})
```

原生JS的页面加载完成之后，除了要等浏览器内核解析完标签创建好DOM对象，还要等标签显示时需要的内容加载完成。

比如说：iframe、img等内容

2、他们触发的顺序？

答：JQuery的页面加载完成之后先执行，再执行原生js的页面加载完成之后

3、他们执行的次数？

答：原生js的页面加载完成之后，只会加载最后一次的完成函数

JQuery会按照注册的顺序依次完成每一个完成函数

**其他的事件处理方法：**

**click()**

它可以绑定单击事件，以及触发单击事件（触发其他的，通过对象调用click()而不传参

**mouseover()**

鼠标移入事件

**mouseout()**

鼠标移出事件

**bind()**

可以给元素一次性绑定一个或多个事件

```javascript
$("h5").bind("click mouseover",function(){
	console.log("这是bind绑定的两个事件");
})
```

**one()**

使用上跟bind一样，但是one方法绑定的事件只会响应一次

```javascript
$("h5").one("click mouseover",function(){
	console.log("这是bind绑定的两个事件");
})
```

**unbind()**

跟bind的相反的操作，解绑事件

**live()**

也是用来绑定事件，它可以用来绑定选择器匹配的所有元素的事件，哪怕是后来动态创建出来的也有效。

### 事件的冒泡

#### 何为事件冒泡？

事件的冒泡是指，父子元素同时监听同一个事件。当触发子元素的事件的时候，同一个事件也被传递到了父元素的事件里去响应。这是默认行为。

#### 阻止冒泡：

只需要在子元素的事件函数内，return false即可。

### 事件对象

#### 概念

事件对象，是封装有触发的事件信息的一个JavaScript对象。

#### 获取方式

在给元素绑定事件的时候，在事件的function()声明中添加一个，我们一般称为event的参数，如function(event)，这个event就是JavaScript传递事件处理函数的事件对象。

**原生js获取事件对象**

```javascript
// 1.原生javascript获取 事件对象
window.onload = function () {
    document.getElementById("areaDiv").onclick = function (event) {
    console.log(event);
    }
}
```

**JQuery获取事件对象**

```javascript
// 2.JQuery获取事件对象
$(function(){
	$(div).click(function(event){
		console.log(event);
	})
})
```

##### 具体实践

**可用于当bind同时对多个事件绑定同一个函数时，获取当前的操作是哪一个操作**

```javascript
$("#areaDiv").bind("mouseover mouseout",function (event) {
    if (event.type == "mouseover") {
    	console.log("鼠标移入");
    } else if (event.type == "mouseout") {
    	console.log("鼠标移出");
    }
});
```

