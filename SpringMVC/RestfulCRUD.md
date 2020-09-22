# RestfulCRUD

## Rest格式

增删改查的请求地址： 	/资源名/资源标识

eg:

/emp/1	GET:请求id为1的员工信息

/emp/1	POST:添加id为1的员工

/emp/1	PUT:更新id为1的员工信息

/emp/1	DELETE:删除id为1的员工信息

/emps	  GET:请求所有的员工信息

## 前端

### Spring的表单标签回显

- 通过SpringMVC的表单标签可以实现将模型数据中的属性和HTML的表单元素相绑定，以实现表单数据更便捷编辑和表单值的回显

- SpringMVC认为，表单数据中的每一项最终都是要回显的

  - path指定的是一个属性，这个属性是从隐含模型（请求域中取出的某个对象中的属性）；
  - path指定的每一个属性，请求域中必须有一个对象，拥有这个属性
    - 这个对象就是请求域中command的值 
  - 可以在表单中指定modelAttribute属性，决定请求域中"command"对象的key。
    - 以前我们表单标签会从请求域中获取一个key为command的对象，用他的属性对应着path来填充我们的表单项，现在通过这个属性的指定可以改变key为我们想要的。

  path：

  1. 当做原生的name项
  2. 自动回显隐含模型中某个对象对应这个属性的值

  < form:select > :

  ```xml
  <form:select path="department.id" items="${depts}" itemLabel="departmentName" itemValue="id"></form:select>
  ```

  

  1. items:指定要遍历的集合，会自动遍历,遍历出的每一个元素是一个department对象
  2. itemLabel:指定对象中的一个属性，作为option显示的信息
  3. itemValue:指定对象中的一个属性，作为提交的值

