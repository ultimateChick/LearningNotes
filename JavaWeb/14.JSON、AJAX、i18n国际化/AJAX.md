# AJAX

## 概念

ajax是一种浏览器通过js异步发起请求，局部更新页面的技术。

## 原生AJAX的使用例

```javascript
<script type="text/javascript">
    function ajaxRequest() {
    // 				1、我们首先要创建XMLHttpRequest 
    var xmlHttpRequest = new XMLHttpRequest();
    // 				2、调用open方法设置请求参数
    xmlHttpRequest.open("get", "http://localhost:8080/14_json_ajax_i18n/ajaxServlet?action=rawAjax", true);//true表示该请求是异步的
    // 				4、在send方法前绑定onreadystatechange事件，处理请求完成后的操作。
    xmlHttpRequest.onreadystatechange = function(){
        if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200){
            document.getElementById("div01").innerHTML = xmlHttpRequest.responseText;
        }
    };
    // 				3、调用send方法发送请求
    xmlHttpRequest.send();

}
</script>
```

## 使用JQuery封装的AJAX

### 经典AJAX

```javascript
// ajax请求
$("#ajaxBtn").click(function(){
    $.ajax({
        url:"http://localhost:8080/14_json_ajax_i18n/ajaxServlet",
        data:{action: "jQueryAjax"},
        type:"GET",
        success:function (data) {
            $("#msg").html(".ajax 编号:" + data.id + " , 姓名:" + data.name);
        },
        dataType: "json"
    })
});
```

### 确定请求类型的简化ajax

$.get(url, data, callback, datatype) 方法

```javascript
$.get("http://localhost:8080/14_json_ajax_i18n/ajaxServlet","action=jQueryGet",function(data){
    $("#msg").html(".get 编号:" + data.id + " , 姓名:" + data.name);
},"json");
```

$.post(url, data, callback, datatype) 方法

```javascript
$.post("http://localhost:8080/14_json_ajax_i18n/ajaxServlet",{action: "jQueryAjax"},function(data){
    $("#msg").html(".post 编号:" + data.id + " , 姓名:" + data.name);
},"json");
```

$.getJSON(url, data, callback) 方法

```javascript
$.getJSON("http://localhost:8080/14_json_ajax_i18n/ajaxServlet",{action: "jQueryAjax"},function(data){
    $("#msg").html(".post 编号:" + data.id + " , 姓名:" + data.name);
});
```

表单序列化 serialize()

serialize()可以把表单中所有表单项的内容都获取到，并以name=value&name=value的形式进行拼接。