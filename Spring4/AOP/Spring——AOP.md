# Spring——AOP

## 概念

## 步骤

配置好Spring的Bean相关项目后，在配置文件加入如下配置：

```xml
<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```

把横切关注点的代码抽象到切面的类中。

- 切面首先是一个IOC中的Bean，即加入@Component注解
- 切面还需要加入@Aspect注解

在类中声明各种通知：

![image-20200703183256996](C:\Users\q1367\Desktop\Spring4\AOP\通知与切面.png)

- 在方法前加入通知的注解，以@Before为例子

- ![image-20200703183523430](C:\Users\q1367\Desktop\Spring4\AOP\切入点表达式.png)

- 合并切入点表达式

  ![image-20200703183728791](C:\Users\q1367\Desktop\Spring4\AOP\合并切入点表达式.png)

- 让通知访问当前连接点的细节

  ![image-20200703183841906](C:\Users\q1367\Desktop\Spring4\AOP\连接点细节.png)

  ```java
  /**
   * 需要把这个类声明为一个IOC容器，再声明为一个切面
   */
  @Aspect
  @Component
  public class LoggingAspect {
  
      @Before(value = "execution(public int *.add(int,int))")
      public void loggingBefore(JoinPoint joinPoint){
          String MethodName = joinPoint.getSignature().getName();
          System.out.println("方法调用前记录日志");
          System.out.println("连接点为" + MethodName);
      }
  }
  ```

  

