# JSON

## 概念

一种轻量级的数据交换格式，易于人阅读和编写，也易于机器解析和生成。JSON采用完全独立于语言的文本格式，主流的语言对其都实现了很好的支持，使其成为理想的数据交换格式。

#### JSON是一种轻量级的数据交换格式

##### 轻量级：

指的是跟xml作比较，更为轻便，解析更快。

##### 数据交换：

指的是客户端和服务器之间业务数据的传输格式。

## 定义

JSON是由键值对组成，并且由花括号包围，每个键由引号引起来，键和值之间使用冒号进行分割，多组键值对之间使用逗号进行分割。

eg:

```javascript
var jsonObj = {
    "key1": 12,
    "key2": "key2Val",
    "key3": true,
    "key4": [12, "aa"],
    "key5": {
    "key5_1": 11,
    "key5_2": "key5_2Val"
    },
    "key6": [{
    	"key6_1_1": 123
    }, {
    	"key6_2_1": 324,
    	"key6_2_2": false
    }]
}

alert(typeof jsonObj); //输出为object
```

## JSON的访问

即是对**对象**的属性的访问。

**格式：json对象.属性;**

## JSON的两个常用方法

### 两种存在形式

一种是以**对象**的形式存在，我们叫他json对象。

另一种是以**字符串**的形式存在，我们叫他json字符串。

#### 转换函数

JSON.stringify()	把json对象转换成为json字符串，类似于toString()方法

JSON.parse()		把json字符串转换成为json对象

#### 如何选择

一般我们要操作json中数据的时候，需要json对象的格式。

一般我们要在客户端和服务器之间进行数据交换的时候，使用json字符串。

## JSON在服务器（Java）中的使用

**先导入json的jar包，一般使用谷歌提供的Gson。**

### JavaBean和JSON的互转

gson.toJson(Bean对象);  得到关于对象的json字串

gson.fromJson(json字串, JavaBean.class);  得到json字串中含有的bean对象

### List和JSON的互转

### Map和JSON的互转

获得字串的方式都是toJson



从字串中获得对象的方式需要借助 new TypeToken<List对象类型或Map对象类型>(){}.getType()

所获得的type对象传入fromJson方法来实现。