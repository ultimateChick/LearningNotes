# Spring注解驱动

## 给容器中注册组件的方法

1. 包扫描+组件标注注解（@Controller/@Service/@Repository/@Component）
2. @Bean[导入的第三方包里面的组件]
3. @Import[快速的给容器中导入一个组件]
   1. @Import(要导入到容器中的组件)：容器中就会自动注册这个组件，id默认是全类名
   2. ImportSelector：返回需要导入的组件的全类名数组
   3. ImportBeanDefinitionRegistrar：手动注册bean到容器中
4. 使用Spring提供的FactoryBean（工厂Bean
   1. 默认获取到的是工厂bean调用getObject创建的对象
   2. 要获取工厂Bean本身，我们需要给id前面加一个&，BeanFactory接口定义了一个用&表示工厂Bean(FactoryBean)的常量，在getBean方法中会有相应的判断。
   3. 通过获取容器中的已注册的类列表，我们发现factoryBean的id是我们命名的自定义类名，但是其实例化对象的类型是我们在类中实现的getObject和getObjectType定义的

## 配置类

- 用注解@Configuration标注配置类
- @ComponentScan注解可以指定扫描的路径，过滤方法，以及默认过滤器的开闭。
- 此注解还是可重复注解，可以通过重复注解来达到更复杂的扫描策略,或者是用@ComponentScans包含多个@ComponentScan来达成。

- 在类中，我们可以用@Bean注解返回特定类型的方法，此方法返回的类型所含的属性可以认为是xml配置时的<property>

- 给@Bean指定value值，意为id值的设定

- 除了Bean，我们还可以追加@Lazy表示懒加载，@Scope决定Bean的生成策略（单例、原型、请求或者会话），@Conditional决定该Bean是否加入容器

  ```java
  @Configuration
  //@ComponentScan(basePackages = {"com.atguigu.spring"}, includeFilters = {
  //        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Service.class, Controller.class})
  //}, useDefaultFilters = false)
  //@ComponentScan(basePackages = {"com.atguigu.spring"}, includeFilters = {
  //        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Repository.class})
  //}, useDefaultFilters = false)
  //@ComponentScan(basePackages = {"com.atguigu.spring"}, includeFilters = {
  //        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyFilter.class})
  //}, useDefaultFilters = false)
  @ComponentScans({
          @ComponentScan(basePackages = {"com.atguigu.spring"}, includeFilters = {
                  @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyFilter.class})
          }, useDefaultFilters = false),
          @ComponentScan(basePackages = {"com.atguigu.spring"}, includeFilters = {
                  @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Repository.class})
          }, useDefaultFilters = false)
  })
  public class MainConfig {
      @Bean("person01")
      @Lazy
      @Scope("prototype")
      @Conditional(MyCondition.class)
      public Person person01(){
          return new Person(1,"niayi","tailing");
      }
  
  }
  ```

  

## Bean创建与销毁的生命周期

### Bean的生命周期：

bean创建---初始化---销毁的过程

### 容器管理bean的生命周期：

#### 构造

- 单实例Scope：容器在创建的时候就创建Bean
- 多实例Scope：每次getBean时创建对象

#### 初始化

对象创建完成，并赋值完毕，调用init初始化方法

#### 销毁

单实例Scope：容器关闭的时候销毁

多实例Scope：容器不管理这个Bean，销毁方法需要手动调用

#### 初始化和销毁方法

我们可以自定义初始化和销毁方法，容器在bean中进行到特定生命周期时，就会进行初始化和销毁方法。

1. 指定初始化和销毁方法

   1. xml配置方法中，指定init-method属性和destroy-method属性
   2. 注解方式，则如例子：@Bean(initMethod = "init", destroyMethod = "destroy")，其中字符串中的值是定义在Bean所指向类型中的方法名称

2. 利用接口实现（实现InitializingBean定义初始化逻辑，实现Disposable定义销毁逻辑）

3. 使用JSR250规范定义的两个注解

   1. @PostConstruct：在bean创建完成并且属性赋值完成，来执行初始化方法
   2. @PreDestroy：在容器销毁bean之前通知我们进行清理工作

   **BeanPostProcessor**【interface】：Bean的后置处理器

   在Bean初始化前后进行一些初始化工作

   postProcessBeforeInitialization：初始化方法调用之前进行后置处理工作。

   postProcessAfterInitialization：初始化方法调用之后做后置处理工作。

##### 初始化后置处理器原理

1. 在doCreateBean方法中，我们对Bean的属性进行装配，populateBean

2. 再进入initializeBean，遍历后置处理器并调用（PostProcessor接口实现）

   {

   applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

   invokeInitMethods(beanName, wrappedBean, mbd);

   applyBeanPostProcessorAfterInitialization(wrappedBean, beanName);

   }

##### Spring底层对PostProcessor的应用

Bean赋值、组件注入、自动装配、注解生命周期方法、@Async等等都是依靠PostProcessor进行功能预先实现

## AOP篇

### BeanFactory接口图解

![BeanFactory继承树](C:\Users\q1367\Desktop\Spring注解驱动开发\BeanFactory图解.jpg)

### ApplicationContext接口组成

![img](C:\Users\q1367\Desktop\Spring注解驱动开发\ApplicationContext接口组成.jpg)

```
/**
 * 【动态代理】
 * 指在程序运行期间动态地将某段代码切入到指定方法、指定位置进行运行的编程方式
 * 1、导入aop模块：SpringAOP:(spring-aspects)
 * 2、创建业务逻辑类（MathCalculator）；在业务逻辑运行的时候将日志进行打印（相对位置）
 *
 * 三个步骤：
 * 1）分别创建负责业务逻辑的类和执行通知方法的切面类
 * 2）在切面类中通过切点表达式和目标方法方位注解的方式，创造我们想要的通知方法
 * 3)将业务逻辑代码与切面类都加入到容器中，并且用@Aspect告诉Spring哪个是切面类
 *      对象加入容器的方式：
 *      1.@ComponentScan+组件注解（@Component、@Repository、@Service、@Controller）等 @Aspect不能单独使用
 *      2.直接在配置类中标注@Bean，进行对象创建
 *      3.通过实现xxxAware接口，把我们想要的内部组件注入到相应引用中
 *
 * @author lingfu
 * @create 2020-07-08 21:15
 *
 *
 */
```

![image-20200709154442223](C:\Users\q1367\Desktop\Spring注解驱动开发\后置处理器列表.png)

![image-20200710124041703](C:\Users\q1367\Desktop\Spring注解驱动开发\拦截顺序.png)