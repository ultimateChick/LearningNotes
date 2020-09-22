# Spring4

## 引入

Spring特性：

轻量级：Spring是非侵入性的-基于Spring开发的应用中对象可以不依赖于Spring的API

依赖注入：（DI——dependency injection、IOC）

面向切面编程：（AOP——aspect oriented programming）

容器：Spring是一个容器，因为它包含并且管理应用对象的生命周期

框架：Spring实现了使用简单的组件配置组合成一个复杂的应用，在Spring中可以使用XML和Java注解组合这些对象

一站式：在IOC和AOP的基础上可以整合各种企业应用的开源框架和优秀的第三方类库（实际上Spring自身也提供了展现层的SpringMVC和持久层的Spring JDBC）

## Bean的配置

配置形式：基于XML文件的方式；基于注解的方式

Bean的配置方式：通过全类名（反射）、通过工厂方法（静态工厂方法&实例工厂方法）、FactoryBean

IOC容器：BeanFactory & ApplicationContext概述

依赖注入的方式：属性注入；构造器注入

注入属性值细节

自动转配

bean之间的关系：继承、依赖

bean的作用域：singleton；prototype；WEB环境作用域

使用外部属性文件

spEL

IOC容器中Bean的生命周期

Spring 4.x 新特性：泛型依赖注入

### IOC和DI解释

**IOC(Inversion of Control):**其思想是反转资源获取的方向。传统的资源查找方式要求组件向容器发起请求查找资源。作为回应，容器适时地返回资源。而应用了IOC之后，则是容器主动的将资源推送给他所管理的组件，而组件要做的仅是选择一种合适的方式来接受资源。这种行为也被称为查找的被动形式。

**DI(Dependency Injection):**IOC的另一种表述方式：即组件以一些预先定义好的方式（例如：setter方法）接收来自如容器的资源注入。相对于IOC而言，这种表述更直接。

![image-20200702163729015](C:\Users\q1367\Desktop\Spring4\IOC演进之分离接口与实现.png)

![image-20200702163805587](C:\Users\q1367\Desktop\Spring4\采用工厂模式.png)

![image-20200702163829591](C:\Users\q1367\Desktop\Spring4\采用翻转控制.png)

### XML配置Bean

```xml
    <bean id="helloworld" class="com.atguigu.pojo.Hello">
        <property name="name" value="lingfu"/>
    </bean>
```

class：bean的全类名，通过反射的方式在IOC容器中创建Bean，所以要求Bean中必须拥有无参的构造器。

id：标识容器中bean，id唯一。

### IOC容器

```java
@Test
public void hello(){
    //ApplicationContext代表IOC容器
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationConfiguration.xml");
    Hello helloworld = (Hello) ctx.getBean("helloworld");
    helloworld.hello();
}
```

在SpringIOC容器读取Bean配置创建Bean实例之前，必须对它进行实例化。只有在容器实例化后，才可以从IOC容器里获取Bean实例并使用。

Spring提供了两种类型的IOC容器实现。

**BeanFactory：IOC容器的基本实现。**

**ApplicationContext：提供了更多的高级特性，是BeanFactory的子接口。**

BeanFactory是Spring框架的基础设施，面向Spring本身；

ApplicationContext面向使用Spring框架的开发者，几乎所有的应用场合都直接使用ApplicationContext而非底层的BeanFactory。

无论使用何种方式，配置文件是相同的。

### ApplicationContext解释

![image-20200702165807581](C:\Users\q1367\Desktop\Spring4\图解ApplicationContext.png)

### 依赖注入的方式

**三种方式：**

- 属性注入
  - ![image-20200702171352925](C:\Users\q1367\Desktop\Spring4\属性注入.png)
- 构造器注入
  - ![image-20200702171418331](C:\Users\q1367\Desktop\Spring4\构造方法注入.png)
- 工厂方法注入（很少使用

#### 注入的细节：

- **字面值：**< value>标签可以封装任意可以用字符串表示的属性值，有特殊字符可以借助< ![CDATA[]]>来包裹起来。
- **Bean之间的引用：**ref属性可以建立bean之间的引用关系。用于注入引用类型的属性值，所指向的对象需要在xml中配置完毕。
  - 也可以在<ref></ref>双标签内声明内部bean。

![image-20200702175018756](C:\Users\q1367\Desktop\Spring4\引用其他的Bean.png)

#### 注入参数详解：

##### null值和级联属性

- 可以使用专用的<null/>元素标签为Bean的字符串或其他对象类型的属性注入null值。

  - ```xml
    <property name="car"><null/></property>
    ```

    

- 和Struts、Hibernate等框架一样，Spring支持级联属性的配置。

  - ```xml
    <property name="car" ref="car"/>
    <property name="car.name" value="aoli"/>
    ```

    

##### 集合属性List/Map/Properties

![image-20200702181637492](C:\Users\q1367\Desktop\Spring4\集合属性注入.png)

![image-20200702184637971](C:\Users\q1367\Desktop\Spring4\集合属性之MAP.png)

##### ![image-20200702184554442](C:\Users\q1367\Desktop\Spring4\properties的使用.png)

配置单例的集合Bean：

![image-20200702184905994](C:\Users\q1367\Desktop\Spring4\配置单例的集合Bean.png)

在需要此集合的时候，用ref属性关联到其id即可。

![image-20200702185208035](C:\Users\q1367\Desktop\Spring4\独立的集合单例.png)

P命名空间的使用：

![image-20200702185651414](C:\Users\q1367\Desktop\Spring4\P命名空间.png)

### XML配置中的Bean自动装配

SpringIOC容器可以自动装配Bean，需要做的仅仅是在<bean>的autowire属性里指定自动装配的模式。

byType（根据类型自动装配）：若IOC容器中有多个与目标Bean类型一致的Bean。在这种情况下，Spring将无法判定哪个Bean最适合该属性，因此无法执行自动装配。

byName（根据名称自动装配）：必须将目标Bean的名称与属性名设置的完全相同。

constructor：当存在多个构造器时很复杂，不推荐使用。

**缺点：**只能一次性对所有引用类型的属性进行装配，且同时最多支持一种方式。

### Bean之间的关系

#### 继承Bean配置

![image-20200702230631606](C:\Users\q1367\Desktop\Hibernate\继承bean配置.png)

#### 依赖Bean配置

 ![image-20200702231255819](C:\Users\q1367\Desktop\Hibernate\Bean依赖.png)

这里的依赖不要求注入，只是需要有所依赖的Bean的存在

### Bean的scope属性作用域

scope属性对应四个值：

1. prototype

2. singleton

   默认值，容器初始化时创建bean实例，在整个容器的生命周期内只创建这一个bean，单例

3. session

4. request 

### 静态工厂方法配置bean实例

![image-20200703112916622](C:\Users\q1367\Desktop\Spring4\静态工厂.png)

![image-20200703112755797](C:\Users\q1367\Desktop\Spring4\静态工厂配置Bean的xml配置.png)

```java
public class StaticFactory {
    private static Map<String, Car> cars = new HashMap<>();
    static{
        cars.put("audi", new Car("R7", "Audi"));
        cars.put("BMW", new Car("X7", "BMW"));
    }
    public static Car getCar(String name){
        return cars.get("name");
    }
}
```

### 实例工厂方法配置Bean

![image-20200703114428350](C:\Users\q1367\Desktop\Spring4\实例工厂.png)

```java
public class InstanceFactory {
    private static Map<String, Car> cars = null;
    public InstanceFactory(){
        cars = new HashMap<>();
        cars.put("audi", new Car("R7", "Audi"));
        cars.put("BMW", new Car("X7", "BMW"));
    }
    public Car getCar(String name){
        return cars.get(name);
    }
}
```

### FactoryBean配置Bean

```java
public class BeanFactory implements FactoryBean<Car> {

    private String brand;

    public BeanFactory(String brand) {
        this.brand = brand;
    }

    @Override
    public Car getObject() throws Exception {
        return new Car("X7", brand);
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

![image-20200703120640803](C:\Users\q1367\Desktop\Spring4\FactoryBean.png)

## 基于注解的Bean配置

### 在Class中扫描组件

![image-20200703131533595](C:\Users\q1367\Desktop\Spring4\特定组件.png)

![image-20200703131451002](C:\Users\q1367\Desktop\Spring4\组件扫描.png)

![image-20200703131911986](C:\Users\q1367\Desktop\Spring4\子节点使用.png)

### 利用注解建立Bean之间的引用关系

![image-20200703142247169](C:\Users\q1367\Desktop\Spring4\组件装配.png)

![image-20200703145239645](C:\Users\q1367\Desktop\Spring4\autowire自动装配.png)

![image-20200703145922818](C:\Users\q1367\Desktop\Spring4\其他自动装配.png)

## spEL表达式

基本格式： #{…}

**字面量**：

**引用Bean、属性和方法：**

- 引用其他对象
- 引用其他对象的属性
- 引用其他方法，还可以链式操作

![image-20200703001816392](C:\Users\q1367\Desktop\Hibernate\spEL1.png)

T()可以引用类的静态方法和资源

**支持运算：**

![image-20200703002053328](C:\Users\q1367\Desktop\Hibernate\spEL2.png)

![image-20200703002121048](C:\Users\q1367\Desktop\Hibernate\image-20200703002121048.png)

## Bean生命周期管理

在xml中声明bean时，可以指定init-method="" 和 destroy-method=""属性

决定Bean在创建和销毁时运行的方法。

![image-20200703103829669](C:\Users\q1367\Desktop\Spring4\生命周期管理.png)

### Bean后置处理器

![image-20200703104007303](C:\Users\q1367\Desktop\Spring4\Bean后置处理器.png)

![image-20200703104119901](C:\Users\q1367\Desktop\Spring4\更细致的生命周期.png)

## Spring4.x新特性 泛型依赖注入

![image-20200703150548448](C:\Users\q1367\Desktop\Spring4\泛型依赖注入.png)

