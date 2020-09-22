# CSS篇

## 一、选择器

### 1.1. 标签名选择器

格式：

标签名{

​	属性：值;

}

标签名选择器，可以决定哪些标签被动地使用这个样式。

### 1.2. id选择器

格式：

#id属性值{

​	属性：值；

}

id选择器，可以让我们通过id属性选择性的去使用这个样式。

### 1.3. class选择器

格式：

.class属性值{

​	属性：值；

}

class类型选择器，可以通过class属性有效的选择性的去使用这个样式。

### 1.4. 组合选择器

组合选择器的格式是：

选择器1，选择器2，...选择器n{

​	属性：值；

​	...

}

组合选择器可以让多个选择器共用同样的样式代码。

## 二、常见样式

#### 1. 颜色（字体颜色）

color: ...

#### 2. 宽度

width

#### 3. 高度

height

#### 4. 背景颜色

background-color: ...

#### 5. 边框

border: [宽度] [border-style] [color]

#### 6. div调整（即盒子模型，值得研究）

margin-left: auto

margin-right: auto

margin-bottom

...

#### 7. 文本居中

text-align: ...

#### 8. 超链接去下划线

​	a{

​	text-decoration: none;

}

#### 9. 表格细线

table{

​	border: 1px solid red;/*设置边框*/

​	border-collapse: collapse;/*将边框合并*/

}

td{

​	border: 1px solid red; /*设置边框*/

}

#### 10. 列表去除修饰

ul{

​	list-style: none;

}