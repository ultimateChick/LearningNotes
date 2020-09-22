# DOM模型

## 一、介绍

DOM 全称是 Document Object Model 文档对象模型

使得我们可以把文档中的标签、属性、文本等转换成对象来管理。

## 二、重点部分

### 2.1. Document对象

![image-20200628101206179](C:\Users\q1367\Desktop\JavaWeb\1.HTML && CSS && JavaScript\DOM图解示例.png)

**Document理解：**

· Document它管理了所有的HTML文档内容。

· Document它是一种树结构的文档，有层级关系。

· 它让我们把所有的标签都对象化。

· 我们可以通过document访问所有的标签对象。

eg:

```html
<body>
<div id = "div01">div01</div>
</body>
```

标签对象化，相当于：

```java
class Dom{

private String id; //代表id属性的值

private String tagName; //代表标签的名称

private Dom parentDom; //代表父节点

private List<Dom> chidren; //代表子节点的集合

private String innerHTML; //代表双标签内部封装的数据

}
```

### 2.2. Document中的方法、属性

### 方法：

#### 查询：

#### ①getElementById()：

返回对拥有指定id的第一个对象的引用。这也告诉我们文档中最好不要使用重复的id。

id：identify，含有一一对应的深意。

#### ②getElementsByName():

返回持有指定name属性的dom对象集合

#### ③getElementsByTagName():

返回属于指定标签的dom对象集合

**注意事项：**

· document对象的三个查询方法，如果有id属性，优先使用getElementById方法查询

接下来是Name和TagName。

· 以上三个方法，一定要在页面执行完成之后，才能查询到dom对象：

​	浏览器在执行页面的时候，代码自上而下执行。

​	如果在加载具体的标签代码之前就进行查询，会得到null。

​	所以一般建议在onload中进行查询，onload必然是在页面加载完毕进行的

#### 创建：

createElement(tagName:String):可以创建指定标签名的dom对象

#### 添加：

appendChild(oChildNode):可以添加一个子节点，oChildNode是要添加的孩子节点

eg:

```html
<script type="text/javascript">
    //现在我们需要使用js代码来创建html标签，并显示在页面上
    //标签的内容就是：
    window.onload = function(){

        let divObj = document.createElement("div");//在内存中暂存
        divObj.innerHTML = "niayigo"; //依然在内存中
        document.body.appendChild(divObj);
    }
</script>
```

```html
    <script type="text/javascript">
        //现在我们需要使用js代码来创建html标签，并显示在页面上
        //标签的内容就是：
        window.onload = function(){
            let divObj = document.createElement("div");//在内存中暂存
            let textObj = document.createTextNode("niayigo");//文本也是一个对象
            divObj.appendChild(textObj);
            document.body.appendChild(divObj);
        }
    </script>
```



### 属性：

childNodes：获取当前节点的所有子节点

firstChild：获取当前节点的第一个子节点

lastChild：获取当前节点的最后一个子节点

parentNode：获取当前节点的父节点

previousSibling：获取当前节点的上一个节点（同级节点

nextSibling：获取当前节点的下一个节点

className：用于获取或设置标签的class属性

innerHTML：表示获取/设置起始标签和结束标签中的内容

innerText：表示获取/设置起始标签和结束标签中的文本