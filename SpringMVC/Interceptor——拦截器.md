# HandlerInterceptor——拦截器

SpringMVC提供了拦截器机制，允许运行目标方法之前进行一些拦截工作，或者目标方法运行之后进行一些其他处理。

和Filter功用一致，Filter是JavaWeb（J2EE）提供的，而HandlerInterceptor是Spring提供的

## 接口定义

![image-20200716111007800](C:\Users\q1367\Desktop\SpringMVC\HandlerInterceptor接口定义.png)

**preHandler：**在目标方法运行之前调用；返回布尔类型，true代表doFilter，false表示不放行

**postHandler：**在目标方法运行之后调用，比如说Filter中写在doFilter后的代码

**afterCompletion：**在请求整个完成之后，来到目标页面之后，一定要来到目标资源来调用

比起Filter，不仅拦截时机更多，回调时传入的参数也更强大了

## 具体使用

1. 编写实现了HandlerInterceptor的类

2. 在SpringMVC配置文件中注册这个拦截器

   1. 配置这个拦截起来拦截那些请求的目标方法

   ```xml
   <mvc:interceptors>
       <!--bean标签指向的拦截器默认拦截所有请求-->
       <bean class="com.atguigu.interceptor.MyFirstInterceptor"/>
       <!--
           配置某个拦截器更详细的信息
           拿到的是mappedInterceptor
        -->
       <mvc:interceptor>
           <mvc:mapping path="/test02"/>
           <bean class="com.atguigu.interceptor.MySecondInterceptor"/>
       </mvc:interceptor>
   </mvc:interceptors>
   ```

**拦截顺序：**

```
preHandle……MyFirstInterceptor
here's test01
after target run
success.jsp
here comes to pages
```

正常运行流程：

拦截器preHandle——目标方法target——拦截器postHandle——页面page——拦截器afterCompletion

其他流程：

1. preHandle不放行，后面的流程消失（不像filter的后置代码

2. 如果页面出错，顺序为：拦截器preHandle——目标方法target——出现错误的页面——拦截器afterCompletion（**只要放行了，afterCompletion就一定会执行**，在多拦截器时不出现页面都会执行afterCompletion

   

## 多拦截器运行

**正常流程：**![image-20200716120525990](C:\Users\q1367\Desktop\SpringMVC\多拦截器顺序.png)

和Filter几乎一样

**异常流程：**

1. 不放行
   1. MySecondInterceptor不放行，他前面已经放行了的拦截器的afterCompletion还是会执行。

流程总结：

preHandle：按照顺序执行

postHandle和afterCompletion：逆序执行

已经放行了的拦截器的afterCompletion总会执行

## 拦截器源码——doDispatch

可以看到拦截器的执行被包裹在try-catch-finally中

整个过程，任何阶段产生异常，都会让该执行的afterCompletion执行

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
            //检查请求多部件性质，支持文件功能
            processedRequest = checkMultipart(request);
            multipartRequestParsed = (processedRequest != request);
			
            // 为当前请求找到匹配的控制器
            // Determine handler for the current request.
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }
```

```java
			//前面我们提到取出的handler被封装成handlerChain，其实就是包含了handler还有拦截器，这里是调用拦截器的preHandle方法
            /*
在HandlerInterceptor注解上有说明三个实现类，分别是UserRoleAuthorizationInterceptor（检查用户权限）、LocaleChangeInterceptor（修改本地时间）、ThemeChangeInterceptor（修改当前主题）。可以看出HandlerInterceptor基本都是对请求的一些预处理和结果封装。
            */
            //如果有任何preHandle返回false，这个方法的调用就返回false，!false为true，执行return;
            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }
			
            // 适配器调用控制器中匹配请求的方法
            // Actually invoke the handler.
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
//目标方法只要正常，就会继续向下走到postHandle
            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }

            applyDefaultViewName(processedRequest, mv);
            //调用执行链中拦截器的postHandle
            mappedHandler.applyPostHandle(processedRequest, response, mv);
```

afterCompletion

```java
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new NestedServletException("Handler dispatch failed", err);
        }
        // 前进到页面
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

**applyPreHandle(processedRequest, response)**

```java
/**
	 * Apply preHandle methods of registered interceptors.
	 * @return {@code true} if the execution chain should proceed with the
	 * next interceptor or the handler itself. Else, DispatcherServlet assumes
	 * that this interceptor has already dealt with the response itself.
	 */
boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HandlerInterceptor[] interceptors = getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
        for (int i = 0; i < interceptors.length; i++) {
            HandlerInterceptor interceptor = interceptors[i];
            if (!interceptor.preHandle(request, response, this.handler)) {
                //有一个拦截器preHandle返回false，就调用先前放行的拦截器的afterCompletion
                triggerAfterCompletion(request, response, null);
                //调用结束返回false给外层doDispatch
                return false;
            }
            //放行，设置该拦截器的排序索引
            this.interceptorIndex = i;
        }
    }
    return true;
}
```

**mappedHandler.applyPostHandle(processedRequest, response, mv);**

```java
/**
	 * Apply postHandle methods of registered interceptors.
	 */
void applyPostHandle(HttpServletRequest request, HttpServletResponse response, @Nullable ModelAndView mv)
    throws Exception {

    HandlerInterceptor[] interceptors = getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
        //逆序执行每一个拦截器的postHandle
        for (int i = interceptors.length - 1; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors[i];
            interceptor.postHandle(request, response, this.handler, mv);
        }
    }
}
```

**正常流程的afterCompletion在processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);中进行**

当render正常进行后，就去触发afterCompletion

```java
		// Did the handler return a view to render?
		if (mv != null && !mv.wasCleared()) {
            //页面渲染,这里不进行异常捕获，而是抛给doDispatch
			render(mv, request, response);
			if (errorView) {
				WebUtils.clearErrorRequestAttributes(request);
			}
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("No view rendering, null ModelAndView returned.");
			}
		}

		if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
			// Concurrent handling started during a forward
			return;
		}
		
//如果渲染正常，就会进行此处的调用
		if (mappedHandler != null) {
			// Exception (if any) is already handled..
			mappedHandler.triggerAfterCompletion(request, response, null);
		}
```

```java
/**
	 * Trigger afterCompletion callbacks on the mapped HandlerInterceptors.
	 * Will just invoke afterCompletion for all interceptors whose preHandle invocation
	 * has successfully completed and returned true.
	 */
void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable Exception ex)
    throws Exception {

    HandlerInterceptor[] interceptors = getInterceptors();
    if (!ObjectUtils.isEmpty(interceptors)) {
//我们进行了记录最后一个放行拦截器的索引，从他开始把之前放行的拦截器的afterCompletion都执行
        for (int i = this.interceptorIndex; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors[i];
            try {
                interceptor.afterCompletion(request, response, this.handler, ex);
            }
            catch (Throwable ex2) {
                logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
            }
        }
    }
}
```

### 图解拦截顺序

#### 单拦截

![image-20200716132311598](C:\Users\q1367\Desktop\SpringMVC\图解拦截顺序.png)

#### 多拦截器

**正常流程**

![image-20200716132440781](C:\Users\q1367\Desktop\SpringMVC\多拦截器正常流程.png)

**异常流程**

![image-20200716132528565](C:\Users\q1367\Desktop\SpringMVC\多拦截器异常流畅.png)