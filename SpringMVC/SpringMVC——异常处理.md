# SpringMVC——异常处理

概念：SpringMVC通过HandlerExceptionResolver处理程序的异常，包括Handler映射、数据绑定以及目标方法执行时发生的异常。

九大组件之一的HandlerExceptionResolver下有很多实现类，分别针对不同情境下的异常处理。

## 源码流程

### 默认异常处理策略加载的处理器

- AnnotationMethodHandlerExceptionResolver

  - 当我们使用mvc:annotation-driven配置后，会使用

    ExceptionHandlerExceptionResolver替换它

- ResponseStatusExceptionResolver

- DefaultHandlerExceptionResolver

### 实际流程

```java
       // Actually invoke the handler.
      mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

      if (asyncManager.isConcurrentHandlingStarted()) {
         return;
      }

      applyDefaultViewName(processedRequest, mv);
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
   processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
}
```

从这段源码中，我们看到如果ha.handle执行有异常，那么下面的拦截器的posthandle方法会被跳过，来到catch代码块，用dispatchException变量保存异常，传入processDispatchResult方法，这个方法是实际渲染页面的入口

```java
/**
 * Handle the result of handler selection and handler invocation, which is
 * either a ModelAndView or an Exception to be resolved to a ModelAndView.
 */
private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
      @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
      @Nullable Exception exception) throws Exception {

   boolean errorView = false;

   if (exception != null) {
      if (exception instanceof ModelAndViewDefiningException) {
         logger.debug("ModelAndViewDefiningException encountered", exception);
         mv = ((ModelAndViewDefiningException) exception).getModelAndView();
      }
      else {
         Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
         mv = processHandlerException(request, response, handler, exception);
         errorView = (mv != null);
      }
   }
```

在方法中我们看到如果异常不为空，如果是ModelAndViewDefinitionException，这个异常会为我们提供一个mv直接供给SpringMVC渲染使用

如果是其他类型的异常，会进入processHandlerException方法尝试获取异常时的mv

```java
/**
 * Determine an error ModelAndView via the registered HandlerExceptionResolvers.
 * @param request current HTTP request
 * @param response current HTTP response
 * @param handler the executed handler, or {@code null} if none chosen at the time of the exception
 * (for example, if multipart resolution failed)
 * @param ex the exception that got thrown during handler execution
 * @return a corresponding ModelAndView to forward to
 * @throws Exception if no error ModelAndView found
 */
@Nullable
protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response,
      @Nullable Object handler, Exception ex) throws Exception {

   // Success and error responses may use different content types
   request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);

   // Check registered HandlerExceptionResolvers...
   ModelAndView exMv = null;
   if (this.handlerExceptionResolvers != null) {
      for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
         exMv = resolver.resolveException(request, response, handler, ex);
         if (exMv != null) {
            break;
         }
      }
   }
   if (exMv != null) {
      if (exMv.isEmpty()) {
         request.setAttribute(EXCEPTION_ATTRIBUTE, ex);
         return null;
      }
      // We might still need view name translation for a plain error model...
      if (!exMv.hasView()) {
         String defaultViewName = getDefaultViewName(request);
         if (defaultViewName != null) {
            exMv.setViewName(defaultViewName);
         }
      }
      if (logger.isTraceEnabled()) {
         logger.trace("Using resolved error view: " + exMv, ex);
      }
      else if (logger.isDebugEnabled()) {
         logger.debug("Using resolved error view: " + exMv);
      }
      WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
      return exMv;
   }

   throw ex;
}
```

如果异常有对应的异常处理器进行解析，那么就返回解析后的ModelAndView给SpringMVC渲染，如果找不到解析器，就把异常抛出给Tomcat，由Tomcat来展示异常结果。

### 集中处理异常的类

我们可以把处理异常的所有方法集中放在一个类中，这个类用注解

@ControllerAdvice

告诉SpringMVC这个类增强了Controller，异常处理解析器在这里面找

##### 处理全局异常

异常处理在选择解析器时会优先使用更加精确的处理方法。全局异常只作用于没有被精确指定的异常情况。

##### 本类和增强类都能处理异常

本类的处理方法优先

### 默认异常处理的作用场景

- ExceptionHandlerExceptionResolver：@ExceptionResolver
  - 这个注解可以指定某个异常类，告诉SpringMVC这个方法专门处理这个类发生的异常
- ResponseStatusExceptionResolver：@ResponseStatus
- DefaultHandlerExceptionResolver：判断是否是SpringMVC自带的异常

#### ExceptionHandlerExceptionResolver

```java
@ExceptionHandler(ArithmeticException.class)
public ModelAndView handleException01(Exception ex){
    //最终还是视图解析器解析的
    ModelAndView modelAndView = new ModelAndView("error");
    modelAndView.addObject("ex", ex);
    return modelAndView;
}
```

这个类表示出现数学异常时由它处理，它返回一个mv对象给SpringMVC，view指向"error"，解析时就交给InternalResourceViewResolver拼串；

同时还在隐含模型中放入了异常对象，最终会放入页面中的request域中。

#### ResponseStatusExceptionResolver

这个注解在自定义异常类上可以快速定义异常信息。