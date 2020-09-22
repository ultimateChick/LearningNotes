# SpringMVC

## 概述

1. Spring为展现层提供的基于MVC设计理念的优秀的Web框架，是目前最主流的MVC框架之一
2. 通过一套MVC注解，让POJO(Plain Old Java Object)成为处理请求的控制器，而无需实现任何接口
3. 支持REST风格的URL请求
4. 采用了松散耦合可插拔组件结构，比其他MVC框架更具扩展性和灵活性

## 图示传统MVC架构

![image-20200713122604720](C:\Users\q1367\Desktop\SpringMVC\传统MVC图解.png)

MVC提倡每一层只编写自己的东西，不写任何其他的代码

控制器{

​	调用业务逻辑处理

​	调整到某个页面

}

分层就是为了解耦，解耦是为了维护方便和分工合作

## SpringMVC理解的MVC图示

![image-20200713122945103](C:\Users\q1367\Desktop\SpringMVC\SpringMVC图示.png)

## HelloWorld

SpringMVC的特点在于有一个前端控制器能拦截所有请求，并智能派发

### 细节：

#### 步骤

1. 运行流程：点击链接请求链接
2. 请求来到tomcat服务器
3. SpringMVC前端控制器收到请求
4. 来看请求地址和RequestMapping标注的哪个匹配，来找到到底使用哪个类的哪个方法
5. 前端控制器找到了目标处理器类和目标方法，直接利用反射执行目标方法
6. 方法执行完成以后，会有一个返回值，SpringMVC认为这个返回值就是要去的页面地址
7. 拿到方法返回值以后，用视图解析器拼串，得到完整的页面地址
8. 拿到页面地址，转发到这个页面

#### RequestMapping

作用：告诉SpringMVC这个方法用来处理什么请求，注：/可以省略，无论写的什么值默认就是从/开始

使用很复杂，后续说明？

#### 配置文件的指定

**前端控制器如果不指定配置文件？**

默认是从/WEB-INF/xxx-servlet.xml

xxx是web.xml中配置的servlet-name

**web.xml配置servlet-mapping的url-pattern细节**

/ /*的区别

```
/和/*都可以认为是拦截所有请求
但是/*还会拦截*.jsp页面，而jsp页面应该是交给tomcat服务器解释运行的
```

1. 所有项目的自己的web.xml都是继承于服务器主web.xml的
2. 服务器的主web.xml中有一个DefaultServlet是url-pattern为/的，它用来服务于静态资源（.html .jpg .css ……，除了jsp和servlet都是
3. 我们的配置中，如果前端控制器拦截/，就可能访问不到静态资源
4. 即前端控制器禁用了tomcat服务器中的DefaultServlet，静态资源会来到前端控制器，看哪个方法的RequestMapping能处理这个静态资源

**为什么动态资源能访问呢？**

1. 主web.xml中配置了JspServlet
2. url-pattern为*.jsp *.jspx
3. 我们拦截/的前端控制器没有覆盖JspServlet
4. 如果是/*，就是覆盖了所有的Servlet
5. 写/，也是为了迎合REST风格的URL地址

##  RequestMapping

作用：使用RequestMapping映射请求，在类定义和方法定义处都可以标注

类定义处：提供初步的请求映射信息，相对于WEB应用的根目录，为类中的方法指定了基本路径

方法处：提供进一步的细分映射信息，相对于类定义处的URL。若类定义处未标注@RequestMapping，则方法处标记的URL相对于WEB应用的根目录

### RequestMapping其他属性

#### （引入）一个作用

映射请求参数、请求方法或请求头

#### method={}:String[]

作用：限定请求方式，默认是所有方法都接受

使用：结合RequestMethod枚举类

```
HTTP协议规定的所有请求方式： 在规定以外的报405方法不支持的错误
【GET】,
HEAD,
【POST】,
PUT,
PATCH,
DELETE,
OPTIONS,
TRACE;
```

#### params={}:String[]

作用：规定请求参数，不满足都会404

params支持**简单的表达式**：

1. param1：表示请求必须包含名为param1的请求参数
2. !param1：表示请求不能包含名为param1的请求参数
3. param1=value1：表示请求必须包含名为param1的请求参数，且值不能为value1
4. {"param1=value1","param2"}:表示请求必须包含名为param1和param2的两个请求参数，且param1参数的值必须为value1
5. param1!=value1：不要求有param1参数，一旦有必须确保值不为value1

#### headers={}:String[]

作用：规定请求头

和params一样支持简单的表达式（能支持什么浏览器能访问我的页面，IE爬

#### consumes={}:String[]

作用：只接受内容类型是哪种的请求，限定请求头中的Content-Type

#### produces={}:String[]

作用：告诉浏览器返回的内容类型是什么，规定请求头中的Content-Type

### Ant风格的资源地址

```java
/**
 * 测试Controller功能中的模糊匹配
 *
 * URL地址可以写模糊的通配符：
 * ?    能替代任意一个字符，必须有这个字符
 * *    能替代任意多个字符，和一层路径，带或不带没区别
 * **   能替代多层路径
 * 
 * 模糊和精确同时匹配的情况下，精确地方法优先
 * *和**同时符合匹配，*优先
 */
@Controller
public class RequestMappingFuzzyController {
    @RequestMapping("/antTest01")
    public String antTest01(){
        System.out.println("精确匹配");
        return "success";
    }

    @RequestMapping("/antTest0?")
    public String antFuzzyTest(){
        System.out.println("模糊匹配一个字符");
        return "success";
    }

    @RequestMapping("/*/antTest0*")
    public String antTestSingleStar(){
        System.out.println("singleStar");
        return "success";
    }

    @RequestMapping("**/antTest1*")
    public String antTestMultiStar(){
        System.out.println("multiStar");
        return "success";
    }

}
```

### @PathVariable映射URL绑定的占位符

作用：

1. 带占位符的URL是Spring3.0新增的功能，使得SpringMVC向REST目标发展
2. 通过@PathVariable可以将URL中占位符参数绑定到控制器处理方法的入参中
3. 只能占一层路径
4. 也可以多层路径都捕捉为入参

例子：

```java
@RequestMapping(value = "/delete/{id}")
public String delete(@PathVariable("id") Integer id){
	userDao.delete(id);
	return "redirect:/user/list.action";
}
```

## REST风格

### 概念解释：

![image-20200713191646093](C:\Users\q1367\Desktop\SpringMVC\REST风格.png)

### 如何从页面发起Get、Post以外的请求方式

1. SpringMVC中有一个Filter，他可以把普通的请求转化成规定形式的请求
   1. Spring提供的Filter为HiddenHttpMethodFilter
   2. filter可以写/*对所有请求（包括资源）生效
2. 来到页面，我们创建一个post类型的表单
3. 表单项中携带一个_method的参数，保存实际想要的http动作

### 高版本tomcat不允许jsp页面处理非常规的请求

开启jsp的 isErrorPage=true，把错误信息封装到Exception对象中即可

 

## 请求处理

SpringMVC获得请求参数带来的各种信息

@RequestParam

@RequestHeader

@CookieValue

### @RequestParam获取请求参数的值

1. 直接给方法入参上写一个和请求参数名相同的变量。这个变量就来接受请求参数上的值；

   带，有值；没带，null

   即请求参数的存在与否无所谓

2. 也可以用@RequestParam注解，注解中指定的value是请求来源链接的传参，后接的方法传参可以自定义名称；但这时候必须确保链接当中有注解指定的项目。

#### RequestParam的属性

- value：指定要获取的参数的key
- required：调整这个参数是否必须
- defaultValue：当没有接收到，默认使用的值

#### RequestParam和PathVariable的区别

PathVariable是直接从路径中的某一层获取值

RequestParam获取的是？后接的传参

**/book/{pathVariable}?requestParam=value**

### @RequestHeader

作用：获取请求头中某个key对应的值

使用和@RequestParam一致，但是必须带有这个注解，方法传参才会去头信息中获取值，@RequestParam可以省略

#### 属性和RequestParam类型和用法也完全一致

### @CookieValue

作用：获取cookie中某对键值对

```java
//传统Servlet获取Cookie
Cookie[] cookies = request.getCookies();
for(Cookie c: cookies){
	if(c.getName().equals("JSESSION")){
		sout(c.getValue());
	}
}
```

#### 属性和RequestParam类型和用法也完全一致

Cookie可以结合required避免很多异常情况（初次请求很多cookie都不存在

### POJO自动封装

在传参中指定相应类型的POJO，如果从RequestMapping指定的地址中可以获得与POJO属性一一对应的参数值，则会自动封装为带有值的对象

### 传入原生API

直接在控制器中的方法的传参加入原生的API类型

```java
public String handle03(HttpSession session, HttpServletRequest request)
```

其他支持传入的原生类型

```java
HttpServletRequest
HttpServletResponse
HttpSession
java.security.Principal
Locale:国际化有关的区域信息对象
InputStream
    ServletInputStream is = request.getInputStream()
OutputStream
    ServletOutputStream outputStream = response.getOutputStream();
Reader
Writer
```

### 请求和响应的乱码处理

#### 请求乱码

##### GET请求：

​	修改server.xml的Connector-8080端口配置项的URIEncoding属性值为UTF-8

##### POST请求：

​	传统方式：在第一次获取请求参数之前设置

​	request.setCharacterEncoding("UTF-8");

​	SpringMVC方式：自己写一个FIlter解决，SpringMVC也提供了一个CharacterEncodingFilter

​	CharacterEncodingFilter的encoding可以指定具体的Encoding编码

​	指定forceEncoding为true可以把响应的编码一起设置

​	

```xml
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
    	<param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
        <init-param>
    	<param-name>forceEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```



#### 响应乱码

resp.setContentType("text/html;charset=utf-8")



#### 结论：

1、安装完Tomcat直接修改server.xml中connector-8080的URIEncoding属性为UTF-8，解决GET编码

2、配置完SpringMVC前端控制器后直接编写CharacterEncodingFilter的配置，设置好POST和响应编码

3、字符编码的Filter需要配置在其他Filter之前，防止其他Filter中去获取了请求参数，导致字符编码Filter失效



## 输出数据

作用：SpringMVC除了在方法上传入原生request和session之外，还有方法可以携带数据给页面

1. 可以在方法处传入Map、或者Model或者ModelMap，这些参数里面保存的所有数据，都会放在request域中，可以给页面获取

   1. **Model:**一个接口，方法如图

      ![image-20200714142244161](C:\Users\q1367\Desktop\SpringMVC\Model接口方法.png)

   2. 原理：

      Map(interface(jdk))																	

      ​		/_\

      ​		||	实现

      ​		||

      ModelMap(extends LinkedHashMap<String, Object>)

      ​		/_\

      ​		||	继承								实现

      ExtendedModelMap----------------------------------------- >Model(interface(spring))

      ​		/_\

      ​		|| 	继承

      BindingAwareModelMap

      Spring底层最终会用BingingAwareModelMap来作为实现类实现功能

2. 方法的返回值可以变为ModelAndView类型，传去模型和视图

   1. 这个方法既包含视图信息（页面地址）也包含模型数据（给页面回显的数据）

      而且数据是放在请求域中

### 给Session域中带数据

使用注解：@SessionAttributes，只能标在类上，对控制器内所有方法生效

**属性：**

- value():String[]

  @SessionAttributes(value="msg")：给BindingAwareModelMap中保存的指定key("msg")的数据，**同时**给session放一份。

-  types():Class<?>[]

  @SessionAttributes(types={String.class})：只要保存的是指定类型的数据，给session就保存一份

**结论：**给session中放数据，请使用原生API

### @ModelAttribute：被MyBatis淘汰了（动态sql取代db操作

#### 业务场景

比如说修改图书，很多场景下我们只是部分字段提供修改，这样在pojo自动封装就会有麻烦，可能有某些字段为空，我们要为了不同的场景设计很多sql，不利于维护。前端输入框设置为read-only不安全。

#### @ModelAttribute效用：修改默认值

##### Book对象的封装原理

- SpringMVC创建一个目标对象（new出来的空对象），每个属性都有**默认值**（null、0等

- 将请求中所有与book对应的属性一一设置过来

  - 解决思路：让SpringMVC直接从【数据库】中取出记录，对取出的记录封装为bean之后，给BindingAwareModelMap进行属性覆盖设置

    这样我们就可以用一个全字段更新的sql应对所有情况

  - ModelAttribute注解的方法中获得对象后，把它存到Map中（在方法传参里声明，这样BindingAwareModelMap会来处理

  - 然后在需要使用此新对象的处理方法，声明的传参处用@ModelAttribute("key")表明使用特定对象，不让spring内部去重新new一个了

##### Book对象的底层原理

- BindingAwareModelMap可以跨方法使用，同一个控制器内部的BindingAwareModelMap是共用的一个

![image-20200714162211381](C:\Users\q1367\Desktop\SpringMVC\隐含模型.png)

## SpringMVC源码

### DispatcherServlet结构分析

![image-20200714173738105](C:\Users\q1367\Desktop\SpringMVC\流程概览图示.png)

这里顺便解释一下SpringMVC中的Servlet的三个层次：

HttpServletBean直接继承自java的HttpServlet，其作用是将Servlet中配置的参数设置到相应的Bean属性上
FrameworkServlet初始化了WebApplicationContext

DispatcherServlet初始化了自身的9个组件（重点）

### 初始化过程

#### HttpServletBean.init()

把配置文件中的属性配置进来

#### FrameworkServlet.initServletBean()

初始化并**刷新**webApplicationContext容器

**上下文怎么理解？**

某一个应用程序在整个生命周期中包含的对象

这里其实会调用springIOC容器的刷新流程，SpringMVC在这里和Spring联系起来了

在常规的Spring刷新过程结束之后，于finishRefresh方法中，通过一次事件的广播通知SpringMVC容器创建完成，接着SpringMVC就开始初始化其九大内置对象。**initStrategies()**

### 业务过程

#### doDispatch(HttpServletRequest request, HttpServletResponse response)

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    boolean multipartRequestParsed = false;
	//异步请求处理器
    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

    try {
        ModelAndView mv = null;
        Exception dispatchException = null;

        try {
            //1、检查是否是文件上传请求
            processedRequest = checkMultipart(request);
            multipartRequestParsed = (processedRequest != request);
			
            // 2、为当前请求找到匹配的处理器
            // Determine handler for the current request.
            mappedHandler = getHandler(processedRequest);
            // 3、如果没有匹配的处理器，就返回错误
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }
			
//4、拿到能执行这个类的所有方法的适配器（反射工具RequestMappingHandlerAdaptor）
            // Determine handler adapter for the current request.
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            // Process last-modified header, if supported by the handler.
            String method = request.getMethod();
            boolean isGet = "GET".equals(method);
            if (isGet || "HEAD".equals(method)) {
                long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    return;
                }
            }
			//前面我们提到取出的handler被封装成handlerChain，其实就是包含了handler还有拦截器，这里是调用拦截器的preHandle方法
            /*
在HandlerInterceptor注解上有说明三个实现类，分别是UserRoleAuthorizationInterceptor（检查用户权限）、LocaleChangeInterceptor（修改本地时间）、ThemeChangeInterceptor（修改当前主题）。可以看出HandlerInterceptor基本都是对请求的一些预处理和结果封装。
            */
            //如果有任何preHandle返回false，这个方法的调用就返回false，!false为true，执行return;
            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }
			
// 5、利用适配器来执行目标方法，跟然根据目标方法的返回值，来决定是将返回值包装为视图名（String），还是视图等等，保存到ModelAndView中
// 目标方法无论怎么写，最终适配器执行完成以后都会将执行后的信息封装成ModelAndView
            // Actually invoke the handler.
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }

            applyDefaultViewName(processedRequest, mv);
            //调用执行链中拦截器的postHandle方法
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new NestedServletException("Handler dispatch failed", err);
        }
// 6、根据适配器调用目标方法后获得的ModelAndView，转发到对应页面，而且ModelAndView中的数据可以从请求域中获取。
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                               new NestedServletException("Handler processing failed", err));
    }
    finally {
        if (asyncManager.isConcurrentHandlingStarted()) {
            // Instead of postHandle and afterCompletion
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            // Clean up any resources used by a multipart request.
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}
```

**初步结论：**

1. 所有请求过来DispatcherServlet收到请求

2. 调用doDispatcher()方法进行处理

   1. getHandler():根据当前请求，拿到能处理当前请求的目标处理器类

      根据当前请求在HandlerMapping中找到这个请求的映射信息，获取到目标处理器类

   2. getHandlerAdapter():根据当前处理器类获取能执行这个处理器方法的适配器（反射工具

      根据当前处理器类找到匹配的HandlerAdapter

   3. 执行handler前置处理器

   4. 使用刚才获得获取到的适配器执行目标方法

   5. 目标方法执行后，返回ModelAndView对象

   6. 执行后置处理器

   7. 根据mv对象转发到具体的页面，并可以在请求request域中拿到模型数据

3. #### getHandler()细节：

   1. 返回类型：HandlerExecutionChain，处理器执行链，执行链中除了找到的处理器类还有一系列的拦截器

   2. ```java
      @Nullable
      protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
          if (this.handlerMappings != null) {
              //处理器映射，保存了每一个处理器能处理哪些请求的映射信息
              for (HandlerMapping mapping : this.handlerMappings) {
                  HandlerExecutionChain handler = mapping.getHandler(request);
                  if (handler != null) {
                      return handler;
                  }
              }
          }
          return null;
      }
      ```

      

   3. ![image-20200714180640040](C:\Users\q1367\Desktop\SpringMVC\处理器映射.png)

   4. handlerMap的值从何而来？

      IOC容器在启动的时候，会扫描RequestMapping信息，建立起urlpattern和处理器类的映射关系，保存到HandlerMapping的handlerMap中
      
      ##### HandlerMapping接口族
      
      1. 他是用来查找Handler的，维护路由与具体逻辑的映射关系，即用户URL与对应的处理类Handler，HandlerMapping并没有规定这个URL与应用的处理类如何映射。所以在接口中仅仅定义了根据一个URL必须返回一个由HandlerExecutionChain代表的处理链，我们可以在这个处理链中添加任意的Handler实例来处理这个URL对应的请求，这样保证了最大灵活性映射关系。
      
         ```java
         public interface HandlerMapping {
         	//@since 4.3.21
         	String BEST_MATCHING_HANDLER_ATTRIBUTE = HandlerMapping.class.getName() + ".bestMatchingHandler";
         	
         	String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HandlerMapping.class.getName() + ".pathWithinHandlerMapping";
         	String BEST_MATCHING_PATTERN_ATTRIBUTE = HandlerMapping.class.getName() + ".bestMatchingPattern";
         	String INTROSPECT_TYPE_LEVEL_MAPPING = HandlerMapping.class.getName() + ".introspectTypeLevelMapping";
         	String URI_TEMPLATE_VARIABLES_ATTRIBUTE = HandlerMapping.class.getName() + ".uriTemplateVariables";
         	String MATRIX_VARIABLES_ATTRIBUTE = HandlerMapping.class.getName() + ".matrixVariables";
         	String PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE = HandlerMapping.class.getName() + ".producibleMediaTypes";
         
         	// 该接口提供的唯一一个方法~~~~
         	@Nullable
         	HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
         
         }
         ```
      
         其继承树为：
      
         ![在这里插入图片描述](C:\Users\q1367\Desktop\SpringMVC\HandlerMapping继承树.jpg)
      
         可以看到它有两大继承主线：MatchableHandlerMapping和AbstractHandlerMapping。
      
         ##### **AbstractHandlerMapping**
      
         其继承图谱为
      
         ![在这里插入图片描述](C:\Users\q1367\Desktop\SpringMVC\AbstractHandlerMapping继承图谱.jpg)
      
         这里的xxxSupport，其实是ServletContextAware和ApplicationContextAware的适配器
      
         其抽象实现为：
      
         ```java
         // 它自己又额外实现了BeanNameAware和Ordered排序接口
         public abstract class AbstractHandlerMapping extends WebApplicationObjectSupport
         		implements HandlerMapping, Ordered, BeanNameAware {
         
         	//默认的Handler,这边使用的Obejct,子类实现的时候,使用HandlerMethod,HandlerExecutionChain等
         	// the default handler for this handler mapping
         	@Nullable
         	private Object defaultHandler;
         	// url路径计算的辅助类、工具类
         	private UrlPathHelper urlPathHelper = new UrlPathHelper();
         	// Ant风格的Path匹配模式~  解决如/books/{id}场景
         	private PathMatcher pathMatcher = new AntPathMatcher();
         
         	// 保存着拦截器们~~~
         	private final List<Object> interceptors = new ArrayList<>();
         	// 从interceptors中解析得到,直接添加给全部handler
         	private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<>();
         
         	// 跨域相关的配置~
         	private CorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
         	private CorsProcessor corsProcessor = new DefaultCorsProcessor();
         
         	// 最低的顺序（default: same as non-Ordered）
         	private int order = Ordered.LOWEST_PRECEDENCE;
         	@Nullable
         	private String beanName;
         	
         	...
         	
         	// 关于UrlPathHelper 的属性的一些设置~~~
         	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {...}
         	public void setUrlDecode(boolean urlDecode) { ... }
         	public void setRemoveSemicolonContent(boolean removeSemicolonContent) { ... }
         	public void setUrlPathHelper(UrlPathHelper urlPathHelper) { ... } //我们也是可议自己指定一个自己的UrlPathHelper 的
         	...
         	// PathMatcher我们也可以自己指定
         	public void setPathMatcher(PathMatcher pathMatcher) { ... }
         
         	// Set the interceptors to apply for all handlers mapped by this handler mapping
         	// 可变参数：可以一次性添加多个拦截器~~~~  这里使用的Object
         	public void setInterceptors(Object... interceptors) {
         		this.interceptors.addAll(Arrays.asList(interceptors));
         	}
         
         	// 设值一个UrlBasedCorsConfigurationSource  Map表示它的一些属性们~~~
         	public void setCorsConfigurations(Map<String, CorsConfiguration> corsConfigurations) { ... }
         	// 重载方法  @since 5.1  Spring5.1之后才有的方法
         	public void setCorsConfigurationSource(CorsConfigurationSource corsConfigurationSource) {
         		Assert.notNull(corsConfigurationSource, "corsConfigurationSource must not be null");
         		this.corsConfigurationSource = corsConfigurationSource;
         	}
         	// Configure a custom {@link CorsProcessor} to use to apply the matched
         	// @since 4.2
         	public void setCorsProcessor(CorsProcessor corsProcessor) {
         		Assert.notNull(corsProcessor, "CorsProcessor must not be null");
         		this.corsProcessor = corsProcessor;
         	}
         	...
         
         	// 这步骤是最重要的。相当于父类setApplicationContext完成了之后，就会执行到这里~~~
         	// 这这步骤可议看出   这里主要处理的都是拦截器~~~相关的内容
         	@Override
         	protected void initApplicationContext() throws BeansException {
         		// 给子类扩展：增加拦截器，默认为空实现
         		extendInterceptors(this.interceptors);
         		// 找到所有MappedInterceptor类型的bean添加到adaptedInterceptors中
         		detectMappedInterceptors(this.adaptedInterceptors);
         		// 将interceptors中的拦截器取出放入adaptedInterceptors
         		// 如果是WebRequestInterceptor类型的拦截器  需要用WebRequestHandlerInterceptorAdapter进行包装适配
         		initInterceptors();
         	}
         
         	// 去容器（含祖孙容器）内找到所有的MappedInterceptor类型的拦截器出来，添加进去   非单例的Bean也包含
         	// 备注MappedInterceptor为Spring MVC拦截器接口`HandlerInterceptor`的实现类  并且是个final类 Spring3.0后出来的。
         	protected void detectMappedInterceptors(List<HandlerInterceptor> mappedInterceptors) {
         		mappedInterceptors.addAll(
         				BeanFactoryUtils.beansOfTypeIncludingAncestors(
         						obtainApplicationContext(), MappedInterceptor.class, true, false).values());
         	}
         
         	// 它就是把调用者放进来的interceptors们，适配成HandlerInterceptor然后统一放在`adaptedInterceptors`里面装着~~~
         	protected void initInterceptors() {
         		if (!this.interceptors.isEmpty()) {
         			for (int i = 0; i < this.interceptors.size(); i++) {
         				Object interceptor = this.interceptors.get(i);
         				if (interceptor == null) {
         					throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
         				}
         				this.adaptedInterceptors.add(adaptInterceptor(interceptor));
         			}
         		}
         	}
         	// 适配其实也很简单~就是支持源生的HandlerInterceptor以及WebRequestInterceptor两种情况而已
         	protected HandlerInterceptor adaptInterceptor(Object interceptor) {
         		if (interceptor instanceof HandlerInterceptor) {
         			return (HandlerInterceptor) interceptor;
         		} else if (interceptor instanceof WebRequestInterceptor) {
         			// WebRequestHandlerInterceptorAdapter它就是个`HandlerInterceptor`，内部持有一个WebRequestInterceptor的引用而已
         			// 内部使用到了DispatcherServletWebRequest包request和response包装成`WebRequest`等等
         			return new WebRequestHandlerInterceptorAdapter((WebRequestInterceptor) interceptor);
         		} else {
         			throw new IllegalArgumentException("Interceptor type not supported: " + interceptor.getClass().getName());
         		}
         	}
         
         
         	protected final HandlerInterceptor[] getAdaptedInterceptors() { ... }
         	// 它只会返回MappedInterceptor这种类型的，上面是返回adaptedInterceptors所有
         	protected final MappedInterceptor[] getMappedInterceptors() { ... }
         
         	// 这个方法也是一个该抽象类提供的一个非常重要的模版方法：根据request获取到一个HandlerExecutionChain
         	// 也是抽象类实现接口HandlerMapping的方法~~~
         	@Override
         	@Nullable
         	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
         		// 根据request获取对应的handler   抽象方法，由具体的子类去实现~~~~
         		Object handler = getHandlerInternal(request);
         		// 若没有匹配上处理器，那就走默认的处理器~~~   默认的处理器也是需要由子类给赋值  否则也会null的
         		if (handler == null) {
         			handler = getDefaultHandler();
         		}
         		// 若默认的处理器都木有  那就直接返回null啦~
         		if (handler == null) {
         			return null;
         		}
         		// 意思是如果是个String类型的名称，那就去容器内找这个Bean，当作一个Handler~
         		if (handler instanceof String) {
         			String handlerName = (String) handler;
         			handler = obtainApplicationContext().getBean(handlerName);
         		}
         
         		// 关键步骤：根据handler和request构造一个请求处理链~~
         		HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);
         
         		// 4.2版本提供了对CORS跨域资源共享的支持  此处暂时略过~
         		if (CorsUtils.isCorsRequest(request)) {
         			...
         		}
         
         		return executionChain;
         	}
         
         	// 已经找到handler了，那就根据此构造一个请求链
         	// 这里主要是吧拦截器们给糅进来~  构成对指定请求的一个拦截器链
         	protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
         		// 小细节：因为handler本身也许就是个Chain，所以此处需要判断一下~
         		HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ? (HandlerExecutionChain) handler : new HandlerExecutionChain(handler));
         
         		// 此处就用到了urlPathHelper来解析request 
         		// 如我的请求地址为：`http://localhost:8080/demo_war_war/api/v1/hello`  那么lookupPath=/api/v1/hello
         		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
         		for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
         			if (interceptor instanceof MappedInterceptor) {
         			
         				// 这里其实就能体现出MappedInterceptor的些许优势了：也就是它只有路径匹配上了才会拦截，没有匹配上的就不会拦截了，处理起来确实是更加的优雅些了~~~~
         				// 备注：MappedInterceptor可以设置includePatterns和excludePatterns等~~~~~
         				MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptor;
         				if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
         					chain.addInterceptor(mappedInterceptor.getInterceptor());
         				}
         			} else {
         				chain.addInterceptor(interceptor);
         			}
         		}
         		return chain;
         	}
         	...
         }
         ```
      
         - 最关键之处在于定义了getHandler的模板方法。
      
         - 而getHandler中最关键在于根据request和handler通过getHandlerExecutionChain方法得到处理链。
      
         - getHandlerExecutionChain方法中，先是利用url解析工具，得到工程路径后的资源请求地址，然后去和调用者传进来的interceptors中的mappedInterceptor做一些匹配，如果匹配成功则加入到拦截链，而普通的HandlerInterceptor就直接加入拦截链
      
           ##### MappedInterceptor
      
           概念：一个带有includePatterns和excludePatterns字符串集合并带有HandlerInterceptor功能的类，就是对某些地址进行特殊包含和排除的包装类。
      
           在Servlet3.0推出的HandlerInterceptor实现类都是final类，因此我们的MappedInterceptor需要拓展时，要通过代理的方式来拓展。

4. #### getHandlerAdapter()

   ```java
   protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
       if (this.handlerAdapters != null) {
           for (HandlerAdapter adapter : this.handlerAdapters) {
               if (adapter.supports(handler)) {
                   return adapter;
               }
           }
       }
       throw new ServletException("No adapter for handler [" + handler +
                                  "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
   }
   ```

   

   1. ![image-20200714182643390](C:\Users\q1367\Desktop\SpringMVC\handlerAadapter.png)
   2. 在support方法中进行匹配，成功则返回该适配器

### SpringMVC的九大组件

#### 引入：getHandler和getHandlerAdapter使用的handlerMappings和handlerAdapters是怎么获得的

我们注意到DispatcherServlet类中有九个引用类型的组件

```java
	/** MultipartResolver used by this servlet. */
	//文件上传解析器
	
	@Nullable
	private MultipartResolver multipartResolver;

	/** LocaleResolver used by this servlet. */
	//区域信息解析器，和国际化有关
	@Nullable
	private LocaleResolver localeResolver;

	/** ThemeResolver used by this servlet. */
	//主题解析器，强大的主题效果更换
	@Nullable
	private ThemeResolver themeResolver;

	/** List of HandlerMappings used by this servlet. */
	//Handler映射
	@Nullable
	private List<HandlerMapping> handlerMappings;

	/** List of HandlerAdapters used by this servlet. */
	//Handler适配器
	@Nullable
	private List<HandlerAdapter> handlerAdapters;

	/** List of HandlerExceptionResolvers used by this servlet. */
	//SpringMVC异常解析器
	@Nullable
	private List<HandlerExceptionResolver> handlerExceptionResolvers;

	/** RequestToViewNameTranslator used by this servlet. */
	//请求到视图名的翻译器
	@Nullable
	private RequestToViewNameTranslator viewNameTranslator;

	/** FlashMapManager used by this servlet. */
	//FlashMap+Manager：允许SpringMVC在重定向时携带数据
	@Nullable
	private FlashMapManager flashMapManager;

	/** List of ViewResolvers used by this servlet. */
	//视图解析器（InternalResourceViewResolver
	@Nullable
	private List<ViewResolver> viewResolvers;

```

##### 逐组件解析

**MultipartResolver**

- 用于处理文件上传，当收到请求时，DispatcherServlet#checkMultipart()方法会调用MultipartResolver#isMultipart()方法判断请求中是否包括文件，如果请求中包含文件，则调用MultipartResolver#resolveMultipart()方法对请求的数据进行解析。

- 然后将文件数据解析成MultipartFile并封装在MultipartHttpServletRequest（继承了HttpServletRequest）对象中，最后传递给Controller。

它有两个实现类：

![在这里插入图片描述](C:\Users\q1367\Desktop\SpringMVC\MultipartResolver.png)

CommonsMultipartResolver 使用 commons Fileupload 来处理 multipart 请求，所以在使用时，必须要引入相应的 jar 包；
StandardServletMultipartResolver 是基于 Servlet 3.0来处理 multipart 请求的(基于request.getParts()方法)，使用支持 Servlet 3.0的容器

****

**共同点：全部都是接口，接口就是规范**

**参考书籍：《看透SpringMVC源代码分析与实践》**

#### 九大组件初始化

```java
//	方法在onrefresh()中调用，这是Spring留给子类进行扩展的方法
protected void initStrategies(ApplicationContext context) {
    initMultipartResolver(context);
    initLocaleResolver(context);
    initThemeResolver(context);
    initHandlerMappings(context);
    initHandlerAdapters(context);
    initHandlerExceptionResolvers(context);
    initRequestToViewNameTranslator(context);
    initViewResolvers(context);
    initFlashMapManager(context);
}
```

用initHandlerMappings来进行说明

可以在web.xml中修改DispatcherServlet的某些默认属性

利用<init-param></init-param>

```java
private void initHandlerMappings(ApplicationContext context) {
    this.handlerMappings = null;

    if (this.detectAllHandlerMappings) {
        // Find all HandlerMappings in the ApplicationContext, including ancestor contexts.
        Map<String, HandlerMapping> matchingBeans =
            BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerMappings = new ArrayList<>(matchingBeans.values());
            // We keep HandlerMappings in sorted order.
            AnnotationAwareOrderComparator.sort(this.handlerMappings);
        }
    }
    else {
        try {
            HandlerMapping hm = context.getBean(HANDLER_MAPPING_BEAN_NAME, HandlerMapping.class);
            this.handlerMappings = Collections.singletonList(hm);
        }
        catch (NoSuchBeanDefinitionException ex) {
            // Ignore, we'll add a default HandlerMapping later.
        }
    }

    // Ensure we have at least one HandlerMapping, by registering
    // a default HandlerMapping if no other mappings are found.
    if (this.handlerMappings == null) {
        this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
        if (logger.isTraceEnabled()) {
            logger.trace("No HandlerMappings declared for servlet '" + getServletName() +
                         "': using default strategies from DispatcherServlet.properties");
        }
    }
}
```

**总结：**

- 组件的初始化，就是去容器中找这个组件，如果没有找到就用默认的配置，有的从properties中读取，有的置空
- 组件可用id和类型两种方式查找

### 难点：方法调用时如何确定参数

**例子：目标方法**

```java
public String updateBook(@RequestParam(value="author") String author,Map<String, Object> map, HttpServletRequest request, @ModelAttribute("haha")Book book){
    
}
```

关注点:mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

​			mav = invokeHandlerMethod(request, response, handlerMethod);

**RequestMappingHandlerAdapter.java**

```java
@Nullable
protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
	//请求的封装
    ServletWebRequest webRequest = new ServletWebRequest(request, response);
    try {
        WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
        //取得封装数据的BindingAwareModelMap，隐含模型，服务于mav中的model
        ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
		//经过方法解析，拿到处理器类中能实际执行路由的逻辑方法
        ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
        if (this.argumentResolvers != null) {
            //参数
            invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
        }
        if (this.returnValueHandlers != null) {
           invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
        }
        //添加数据绑定
        invocableMethod.setDataBinderFactory(binderFactory);
        invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
        //如果有ModelAttribute注解的类，在这里面进行模型初始化，流程和调用目标方法的流程接近，
        //只是多了一步注入：在getMethodArgumentValues()方法中，我们会把mavContainer也传进来
        modelFactory.initModel(webRequest, mavContainer, invocableMethod);
        mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

        AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
        asyncWebRequest.setTimeout(this.asyncRequestTimeout);

        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
        asyncManager.setTaskExecutor(this.taskExecutor);
        asyncManager.setAsyncWebRequest(asyncWebRequest);
        asyncManager.registerCallableInterceptors(this.callableInterceptors);
        asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);

        if (asyncManager.hasConcurrentResult()) {
            Object result = asyncManager.getConcurrentResult();
            mavContainer = (ModelAndViewContainer) asyncManager.getConcurrentResultContext()[0];
            asyncManager.clearConcurrentResult();
            LogFormatUtils.traceDebug(logger, traceOn -> {
                String formatted = LogFormatUtils.formatValue(result, !traceOn);
                return "Resume with async result [" + formatted + "]";
            });
            invocableMethod = invocableMethod.wrapConcurrentResult(result);
        }
		//!!!方法的实际调用!!!
        invocableMethod.invokeAndHandle(webRequest, mavContainer);
        if (asyncManager.isConcurrentHandlingStarted()) {
            return null;
        }

        return getModelAndView(mavContainer, modelFactory, webRequest);
    }
    finally {
        webRequest.requestCompleted();
    }
}
```

#### 执行目标方法的细节（以RequestMapping注解的方法处理为例

**ServletInvocableHandlerMethod.java**

在这里方法的主体是来进行返回值处理

```java
/**
	 * Invoke the method and handle the return value through one of the
	 * configured {@link HandlerMethodReturnValueHandler HandlerMethodReturnValueHandlers}.
	 * @param webRequest the current request
	 * @param mavContainer the ModelAndViewContainer for this request
	 * @param providedArgs "given" arguments matched by type (not resolved)
	 */
public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
	//!!!此处具体执行目标方法!!!
    Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
    setResponseStatus(webRequest);

    if (returnValue == null) {
        if (isRequestNotModified(webRequest) || getResponseStatus() != null || mavContainer.isRequestHandled()) {
            disableContentCachingIfNecessary(webRequest);
            mavContainer.setRequestHandled(true);
            return;
        }
    }
    else if (StringUtils.hasText(getResponseStatusReason())) {
        mavContainer.setRequestHandled(true);
        return;
    }

    mavContainer.setRequestHandled(false);
    Assert.state(this.returnValueHandlers != null, "No return value handlers");
    try {
        this.returnValueHandlers.handleReturnValue(
            returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
    }
    catch (Exception ex) {
        if (logger.isTraceEnabled()) {
            logger.trace(formatErrorForReturnValue(returnValue), ex);
        }
        throw ex;
    }
}
```

**InvocableHandlerMethod.java**

解析方法参数后执行调用，返回值给nvokeAndHandle方法

所有方法调用最终指向的关键方法（ModelAttribute、RequestMapping

```java
@Nullable
public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,Object... providedArgs) throws Exception {
	//！！！解析方法参数！！！
    Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
    if (logger.isTraceEnabled()) {
        logger.trace("Arguments: " + Arrays.toString(args));
    }
    return doInvoke(args);
}
```

**InvocableHandlerMethod.java**

```java
/**
	 * Get the method argument values for the current request, checking the provided
	 * argument values and falling back to the configured argument resolvers.
	 * <p>The resulting array will be passed into {@link #doInvoke}.
	 * @since 5.1.2
	 */
protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {

    MethodParameter[] parameters = getMethodParameters();
    if (ObjectUtils.isEmpty(parameters)) {
        return EMPTY_ARGS;
    }
	//和参数个数一致长度的Object数组
    Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
        MethodParameter parameter = parameters[i];
        parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
        args[i] = findProvidedArgument(parameter, providedArgs);
        //如果找到了providedArgument，就不需要后续解析，跳过这个参数
        if (args[i] != null) {
            continue;
        }
        if (!this.resolvers.supportsParameter(parameter)) {
            throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
        }
        try {
            //！！！具体解析当前索引指向的参数！！！
            args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
        }
        catch (Exception ex) {
            // Leave stack trace for later, exception may actually be resolved and handled...
            if (logger.isDebugEnabled()) {
                String exMsg = ex.getMessage();
                if (exMsg != null && !exMsg.contains(parameter.getExecutable().toGenericString())) {
                    logger.debug(formatArgumentError(parameter, exMsg));
                }
            }
            throw ex;
        }
    }
    return args;
}
```

**HandlerMethodArgumentResolverComposite.java**

```java
/**
	 * Iterate over registered
	 * {@link HandlerMethodArgumentResolver HandlerMethodArgumentResolvers}
	 * and invoke the one that supports it.
	 * @throws IllegalArgumentException if no suitable argument resolver is found
	 */
@Override
@Nullable
public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
    //拿到具体的参数解析器（这里是根据参数的信息，比如说类型、注解
    HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
    if (resolver == null) {
        throw new IllegalArgumentException("Unsupported parameter type [" + parameter.getParameterType().getName() + "]. supportsParameter should be called first.");
    }
    //进行具体的参数解析
    return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
}
```

****

#### 各类型参数解析器

（旧版本的SpringMVC对参数的解析是杂糅所有情况到一个方法，通过各种条件判断来进行的；显然会有很多不必要的操作执行；5.1.2版本开始引入针对不同类型的参数的特定参数解析器，使得流程更清晰，更好维护。）

**MapMethodProcessor.java**

```java
@Override
@Nullable
public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

   Assert.state(mavContainer != null, "ModelAndViewContainer is required for model exposure");
   return mavContainer.getModel();
}
```

- 这也是为什么我们在参数中传入Map、ModelMap、Model类型最终是指向对BindAwareModelMap的原因，在这里我们的参数被转为指向这个隐含模型。

**ServletRequestMethodArgumentResolver.java**

用来处理入参中声明的原生request对象

```java
@Override
public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

    Class<?> paramType = parameter.getParameterType();

    // WebRequest / NativeWebRequest / ServletWebRequest
    if (WebRequest.class.isAssignableFrom(paramType)) {
        if (!paramType.isInstance(webRequest)) {
            throw new IllegalStateException(
                "Current request is not of type [" + paramType.getName() + "]: " + webRequest);
        }
        return webRequest;
    }

    // ServletRequest / HttpServletRequest / MultipartRequest / MultipartHttpServletRequest
    if (ServletRequest.class.isAssignableFrom(paramType) || MultipartRequest.class.isAssignableFrom(paramType)) {
        return resolveNativeRequest(webRequest, paramType);
    }

    // HttpServletRequest required for all further argument types
    return resolveArgument(paramType, resolveNativeRequest(webRequest, HttpServletRequest.class));
}
```



**POJO注入**

![image-20200716173914830](C:\Users\q1367\Desktop\SpringMVC\POJO注入处理.png)



****

#### initModel中处理ModelAttribute细节

现状：我们可以对参数进行@ModelAttribute注解，从隐含模型中拿出对象注入参数；value指代隐含模型中的key，实际就是拿出key所指向的value

​	我们可以用@ModelAttribute注解方法，在这个方法中通过入参Map、Model、ModelMap可以把对象放置到隐含模型中，这样我们在其他地方就可以通过入参注解取出；而同样的，方法的注解也可以指定value。

​	ModelAttribute方法是提前于处理器内中的其他方法的！且必须执行。

**细节一：**

```
@ModelAttribute(value = "aa")

void myModelAttribute（Map map）

map.put(xxx,xxx);
```

这里指定的value，实际上是作为方法返回值的key值！

假设此处为void返回值，那么调用结束后，隐含模型中有两个对象(xxx=xxx;aa=null);

如果没有指定这个value，就默认用返回值类型的名称小写(void=null)

**细节二：与SessionAttributes的关系**

****

#### 总结

参数处理就两件事：

1. 标注解

   根据注解的信息，最终得到这个注解应该得到的值

2. 没标注解

   检查是否是原生类型，是的话内部注入

   检查是否是map，用mapResolve注入隐含模型

   不是以上情况，则：

   ​	尝试着用类型小写作为key去隐含模型中找匹配对象

   ​	如果该类型或者key被SessionAttributes声明，就去Session中找，找不到则异常

   ​	都不符合，就空参构造建一个