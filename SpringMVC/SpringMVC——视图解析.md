# SpringMVC——视图解析

## 应用：有前置的返回值都独立解析

### forward:——请求转发

作用：

- 转发到一个页面

  后接的地址中的/代表从工程路径开始

  同时禁止了拼串

- 转发到另一个api

### redirect:——重定向

特点：不像原生的重定向，此处的重定向/代表从工程路径开始

作用：

- 重定向到页面和api

## 流程原理

- 方法执行后的返回值会作为页面地址参考，转发或者重定向到页面

- 视图解析器会进行页面地址的拼串

任何方法的返回值，最终都会被包装成ModelAndView对象

![image-20200716183116547](C:\Users\q1367\Desktop\SpringMVC\ModelAndView.png)

然后通过doDispatch中的**processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);**方法展示页面。

在processDispatchResult中通过**render(mv, request, response);**执行渲染

```java
protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Determine locale for request and apply it to the response.
    Locale locale = (this.localeResolver != null ? this.localeResolver.resolveLocale(request) : request.getLocale());
    response.setLocale(locale);

    View view;
    String viewName = mv.getViewName();
    if (viewName != null) {
        // We need to resolve the view name.
        view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
```

****

### 通过resolveViewName得到视图

```java
@Nullable
protected View resolveViewName(String viewName, @Nullable Map<String, Object> model, Locale locale, HttpServletRequest request) throws Exception {

    if (this.viewResolvers != null) {
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(viewName, locale);
            if (view != null) {
                return view;
            }
        }
    }
    return null;
}
```

遍历DispatcherServlet中的ViewResolver对象（九大组件之一），对视图名进行解析，如果能得到视图就返回退出循环

我们先前在SpringMVC配置文件中配置的InternalResourceViewResolver就在这里使用

来自于InternalResourceViewResolver父类AbstractCachingViewResolver中的resolveViewName实现

```java
@Override
@Nullable
public View resolveViewName(String viewName, Locale locale) throws Exception {
    if (!isCache()) {
        return createView(viewName, locale);
    }
    else {
        Object cacheKey = getCacheKey(viewName, locale);
        View view = this.viewAccessCache.get(cacheKey);
        if (view == null) {
            synchronized (this.viewCreationCache) {
                view = this.viewCreationCache.get(cacheKey);
                if (view == null) {
                    // Ask the subclass to create the View object.
                    view = createView(viewName, locale);
                    if (view == null && this.cacheUnresolved) {
                        view = UNRESOLVED_VIEW;
                    }
                    if (view != null && this.cacheFilter.filter(view, viewName, locale)) {
                        this.viewAccessCache.put(cacheKey, view);
                        this.viewCreationCache.put(cacheKey, view);
                    }
                }
            }
        }
        else {
            if (logger.isTraceEnabled()) {
                logger.trace(formatKey(cacheKey) + "served from cache");
            }
        }
        return (view != UNRESOLVED_VIEW ? view : null);
    }
}
```

来自UrlBasedViewResolver（UrlBasedViewResolver的子类、InternalResourceViewResolver的父类）的createView实现

```java
/**
 * Overridden to implement check for "redirect:" prefix.
 * <p>Not possible in {@code loadView}, since overridden
 * {@code loadView} versions in subclasses might rely on the
 * superclass always creating instances of the required view class.
 * @see #loadView
 * @see #requiredViewClass
 */
@Override
protected View createView(String viewName, Locale locale) throws Exception {
   // If this resolver is not supposed to handle the given view,
   // return null to pass on to the next resolver in the chain.
   if (!canHandle(viewName, locale)) {
      return null;
   }

   // Check for special "redirect:" prefix.
   if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
      String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
      RedirectView view = new RedirectView(redirectUrl,
            isRedirectContextRelative(), isRedirectHttp10Compatible());
      String[] hosts = getRedirectHosts();
      if (hosts != null) {
         view.setHosts(hosts);
      }
      return applyLifecycleMethods(REDIRECT_URL_PREFIX, view);
   }

   // Check for special "forward:" prefix.
   if (viewName.startsWith(FORWARD_URL_PREFIX)) {
      String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
       //InternalResourceViewResolver专门解析
      InternalResourceView view = new InternalResourceView(forwardUrl);
      return applyLifecycleMethods(FORWARD_URL_PREFIX, view);
   }

   // Else fall back to superclass implementation: calling loadView.
    //流程比较繁复
   return super.createView(viewName, locale);
}
```

### 接口：ViewResolver

```java
public interface ViewResolver {
	
    // 根据传入的视图名，返回View对象
	/**
	 * Resolve the given view by name.
	 * <p>Note: To allow for ViewResolver chaining, a ViewResolver should
	 * return {@code null} if a view with the given name is not defined in it.
	 * However, this is not required: Some ViewResolvers will always attempt
	 * to build View objects with the given name, unable to return {@code null}
	 * (rather throwing an exception when View creation failed).
	 * @param viewName name of the view to resolve
	 * @param locale the Locale in which to resolve the view.
	 * ViewResolvers that support internationalization should respect this.
	 * @return the View object, or {@code null} if not found
	 * (optional, to allow for ViewResolver chaining)
	 * @throws Exception if the view cannot be resolved
	 * (typically in case of problems creating an actual View object)
	 */
	@Nullable
	View resolveViewName(String viewName, Locale locale) throws Exception;

}
```

****

### 根据得到的view，调用其render方法返回页面

```java
        if (view == null) {
            throw new ServletException("Could not resolve view with name '" + mv.getViewName() + "' in servlet with name '" + getServletName() + "'");
        }
    }
    else {
        // No need to lookup: the ModelAndView object contains the actual View object.
        view = mv.getView();
        if (view == null) {
            throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " + "View object in servlet with name '" + getServletName() + "'");
        }
    }

    // Delegate to the View object for rendering.
    if (logger.isTraceEnabled()) {
        logger.trace("Rendering view [" + view + "] ");
    }
    try {
        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().value());
        }
        view.render(mv.getModelInternal(), request, response);
    }
    catch (Exception ex) {
        if (logger.isDebugEnabled()) {
            logger.debug("Error rendering view [" + view + "]", ex);
        }
        throw ex;
    }
}
```

#### view.render(mv.getModelInternal(), request, response);

```java
/**
 * Prepares the view given the specified model, merging it with static
 * attributes and a RequestContext attribute, if necessary.
 * Delegates to renderMergedOutputModel for the actual rendering.
 * @see #renderMergedOutputModel
 */
@Override
public void render(@Nullable Map<String, ?> model, HttpServletRequest request,
      HttpServletResponse response) throws Exception {

   if (logger.isDebugEnabled()) {
      logger.debug("View " + formatViewName() +
            ", model " + (model != null ? model : Collections.emptyMap()) +
            (this.staticAttributes.isEmpty() ? "" : ", static attributes " + this.staticAttributes));
   }

   Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
   prepareResponse(request, response);
    //放行此方法，页面就来了，根据模型数据渲染页面
   renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
}
```

#### renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);

```java
/**
 * Render the internal resource given the specified model.
 * This includes setting the model as request attributes.
 */
@Override
protected void renderMergedOutputModel(
      Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

   // Expose the model object as request attributes.
    //使得隐含模型中的数据在request域中能获取到
   exposeModelAsRequestAttributes(model, request);

   // Expose helpers as request attributes, if any.
   exposeHelpers(request);

   // Determine the path for the request dispatcher.
   String dispatcherPath = prepareForRendering(request, response);

   // Obtain a RequestDispatcher for the target resource (typically a JSP).
   RequestDispatcher rd = getRequestDispatcher(request, dispatcherPath);
   if (rd == null) {
      throw new ServletException("Could not get RequestDispatcher for [" + getUrl() +
            "]: Check that the corresponding file exists within your web application archive!");
   }

   // If already included or response already committed, perform include, else forward.
   if (useInclude(request, response)) {
      response.setContentType(getContentType());
      if (logger.isDebugEnabled()) {
         logger.debug("Including [" + getUrl() + "]");
      }
      rd.include(request, response);
   }

   else {
      // Note: The forwarded resource is supposed to determine the content type itself.
      if (logger.isDebugEnabled()) {
         logger.debug("Forwarding to [" + getUrl() + "]");
      }
      rd.forward(request, response);
   }
}
```

### 总结

视图解析器只是为了得到试图对象；试图对象才能真正的转发（将模型数据全部放在请求域中），或者重定向到页面。

ModelAndView最后是希望得到能正确显示的View，每种ViewResolver会根据不同的试图场景下把Model做不同的封装（比如请求转发时存放在请求域，重定向通过http传参等等），这个视图甚至可以不是jsp，也可以是excel，JFreeChart等等。

处理器实际上不关心怎么得到页面，只负责产生模型数据。这里实现了充分的解耦。

## 图解

![image-20200716191303027](C:\Users\q1367\Desktop\SpringMVC\图解解析流程.png)

## 概念解释

### 视图

视图是为了渲染模型数据，由视图解析器负责实例化，由于视图是无状态的，所以他们不会有线程安全的问题。

#### 常用的视图实现类

![image-20200716214001225](C:\Users\q1367\Desktop\SpringMVC\常用的视图实现类.png)

#### 常用的视图解析器

![image-20200716214204990](C:\Users\q1367\Desktop\SpringMVC\常用的视图解析器.png)

## 便捷国际化

- springMVC配置中引入ResourceBundleMessageSource，指定basename为i18n；
- 资源文件夹中按格式创建properties
- 国际化页面引入fmt标签库
- 利用fmt:message key="xxx"取出配置

### 注意

- 页面的访问需要经过SpringMVC的处理，即前端控制器拦截；比如说直接访问jsp等操作是不可行的

- 为了避免过多的独立请求转发方法，可以在配置中添加<mvc:view->controller直接指定路由到视图解析的映射；且需要同时开启Annotation功能，不然注解的请求映射会失效

- 这个时候还支持重定向和请求转发前缀的国际化功能

  ```xml
  <!-- 
  path:指定哪个请求
  view-name:指定映射给哪个视图（这里支持视图解析器拼串
  -->
  <mvc:view-controller path="" view-name=""/>
  <mvc:annotation-driven/>
  ```

  

- 根据InternalResourceViewResolver的源码可知，重定向和请求转发分别返回RedirectView和InternalResourceView对象，而非JstlView对象，因此国际化失效；