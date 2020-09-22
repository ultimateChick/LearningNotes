# XML

## 一、简介

#### 概念：

xml是可拓展的标记性语言，强语法语言

#### 作用：

xml的主要作用有：

1、用来保存数据，而且这些数据具有自我描述性

2、它还可以作为项目或者模块的配置文件

3、还可以作为网络传输数据的格式（JSON取代之

## 二、语法

### 目录

**1、文档说明**

**2、元素（标签）**

**3、xml属性**

**4、xml注释**

**5、文本区域（CDATA区）**

### 2.1. 文档声明

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!--
<?xml version="1.0" encoding="UTF-8" ?>
以上内容就是xml文件的声明
version="1.0" 表示xml版本
encoding="UTF-8" 表示xml文件本身的编码
-->
```

### 2.2. 元素

XML元素指的是从（且包括）开始标签直到（且包括）结束标签的部分。

元素可包含其他元素、文本或者两者的混合物。元素也可以拥有属性。

#### 命名规则

· 名称可以含字母、数字以及其他的字符。

· 名称不能以数字或者标点符号开始

· 名称不要以字符“xml”相关开始

· 名称不能有空格

#### 单标签or双标签？

其实没有严格的区分，但是双标签更适合封装属性，单标签需要把所有属性写在一行上。

### 2.3. 属性

xml的属性可以提供额外的信息。每个属性的值必须使用引号引起来

### 2.4. 规则

1、标签必须闭合（不会自动修复。

2、标签对大小写敏感。

3、标签必须正确的嵌套。

4、文档必须有根元素，

​	没有父标签的元素叫顶级元素，

​	根元素是唯一的一个顶级元素。

5、特殊字符需要转义

6、文本区域（CDATA区域）

​	CDATA语法可以告诉xml解析器，CDATA域里的文本内容是纯文本，而不需要转义

```xml
    <book>
        <name>从入门到放弃</name>
        <author>
            <![CDATA[
                niayige>>>>>>>>>
            ]]>
        </author>
    </book>
```

## 三、XML解析技术

不管是html还是xml文件都是**标记性文档**，都可以使用w3c组织制定的dom技术来解析。



早期JDK为xml解析提供了两种解析技术：**DOM与Sax**（Sax已经过时但是仍然要了解）。

**Sax即Simple API for XML**，它是由SUN公司提供的对DOM解析技术进行的升级。

以类似事件机制通过回调告诉用户当前正在解析的内容。

它是一行一行的读取xml文件进行解析的。不会创建大量的DOM对象。

所以它在解析xml的时候，在内存的使用上和性能上都优于DOM解析。

**第三方的解析：**

jdom在dom的技术上进行了封装、**dom4j**又对jdom进行了封装。

pull主要用在安卓开发，是跟Sax非常相似的事件机制来处理。



### dom4j解析技术

#### 编程步骤

第一步：先加载xml文件创建Document对象

第二步：通过Document对象拿到跟元素对象

第三步：通过根元素.elements（标签名）；可以返回一个集合，这个集合里放着

```java
//读取books.xml文件生成Book类
@Test
public void test2() throws Exception{
    // 1.读取books.xml文件
    SAXReader reader = new SAXReader();
    //在Junit测试中，相对路径是从模块名开始算。
    Document document = reader.read("src/books.xml");
    // 2.通过Document对象获取根元素
    Element rootElement = document.getRootElement();
    System.out.println(rootElement);
    // 3.通过根元素获取book标签对象
    // element()和elements() 都是通过标签名查找子元素
    List<Element> books = rootElement.elements("book");
    // 4.遍历，处理每个book标签转换为Book类
    for (Element book: books){
        // asXML()把标签对象，转换为标签字符串。
        System.out.println(book.asXML());
        Element nameElement = book.element("name");
        // getText()方法，可以获取标签中的文本内容。
        String nameText = nameElement.getText();
        // 直接获取指定标签名的文本内容
        String priceText = book.elementText("price");
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceText));
        String author = book.elementText("author").trim();
        //还有一个字段的值在标签的属性上
        String snValue = book.attributeValue("sn");
        Book _book = new Book(snValue, nameText, price, author);
        System.out.println(_book);
    }
}
```

