# Listener

基本性质：

监听对象

监听事件

触发行为

## javaWeb规范中的监听器分类

![image-20200722225545008](C:\Users\q1367\Desktop\JavaWeb\15.补充Listener\监听器分类.png)

按作用对象分成三类接口

1. 监听ServletContext事件的

   1. ServletContextListener：监听ServletContext生命周期事件（服务器启动创建、服务器关闭销毁）
   2. ServletContextAttributeListener：监听域中属性变化的（整个服务器过程的域

2. 监听HttpSession事件的

   1. HttpSessionListener：监听HttpSession的生命周期（session第一次使用的时候创建，把session的id以cookie的方式给到客户端，从而建立起客户端和服务器的对应状态关系；

      session超时（默认30分钟）销毁，还有手动设置失效（session.invalidate()）

   2. HttpSessionAttributeListener:监听session域中属性变化，面对的是**所有**在session中的对象（增删改查）

   3. HttpSessionActivationListener：监听**某个对象**随着HttpSession活化钝化的

   4. HttpSessionBindingListener:监听**某个对象**保存到session中(绑定)和从session中移除(解绑)

3. 监听ServletRequest事件

   1. ServletRequestListener：监听request对象的生命周期（请求进来创建新的request保存请求的详细信息，完成请求给前台响应则销毁
   2. ServletRequestAttributeListener：监听request域中所有属性变化的

按照功能，顺便理清接口方法：

- 监听生命周期

  ServletContextListener：

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `contextDestroyed(ServletContextEvent sce)`Receives notification that the ServletContext is about to be shut down. |
  | `default void`    | `contextInitialized(ServletContextEvent sce)`Receives notification that the web application initialization process is starting. |

  HttpSessionListener

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `sessionCreated(HttpSessionEvent se)`Receives notification that a session has been created. |
  | `default void`    | `sessionDestroyed(HttpSessionEvent se)`Receives notification that a session is about to be invalidated. |

  ServletRequestListener

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `requestDestroyed(ServletRequestEvent sre)`Receives notification that a ServletRequest is about to go out of scope of the web application. |
  | `default void`    | `requestInitialized(ServletRequestEvent sre)`Receives notification that a ServletRequest is about to come into scope of the web application. |

- 监听属性变化

  ServletContextAttributeListener

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `attributeAdded(ServletContextAttributeEvent event)`Receives notification that an attribute has been added to the ServletContext. |
  | `default void`    | `attributeRemoved(ServletContextAttributeEvent event)`Receives notification that an attribute has been removed from the ServletContext. |
  | `default void`    | `attributeReplaced(ServletContextAttributeEvent event)`      |

  HttpSessionAttributeListener

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `attributeAdded(HttpSessionBindingEvent event)`Receives notification that an attribute has been added to a session. |
  | `default void`    | `attributeRemoved(HttpSessionBindingEvent event)`Receives notification that an attribute has been removed from a session. |
  | `default void`    | `attributeReplaced(HttpSessionBindingEvent event)`Receives notification that an attribute has been replaced in a session. |

  ServletRequestAttributeListener

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `attributeAdded(ServletRequestAttributeEvent srae)`Receives notification that an attribute has been added to the ServletRequest. |
  | `default void`    | `attributeRemoved(ServletRequestAttributeEvent srae)`Receives notification that an attribute has been removed from the ServletRequest. |
  | `default void`    | `attributeReplaced(ServletRequestAttributeEvent srae)`Receives notification that an attribute has been replaced on the ServletRequest. |

- session独有的监听器

  HttpSessionActivationListener

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `sessionDidActivate(HttpSessionEvent se)`Notification that the session has just been activated. |
  | `default void`    | `sessionWillPassivate(HttpSessionEvent se)`Notification that the session is about to be passivated. |

  HttpSessionBindingListener

  | Modifier and Type | Method and Description                                       |
  | :---------------- | :----------------------------------------------------------- |
  | `default void`    | `valueBound(HttpSessionBindingEvent event)`Notifies the object that it is being bound to a session and identifies the session. |
  | `default void`    | `valueUnbound(HttpSessionBindingEvent event)`Notifies the object that it is being unbound from a session and identifies the session. |

## 监听器的使用与场景

### 使用

1. 编写实现监听器接口的类
2. 注册到web容器中

#### Session独有的监听器HttpSessionActivationListener、HttpSessionBindingListener

他们分别是监听某个对象随着session的活化钝化还有和session进行绑定的事件，所以这两个接口在对应的对象类实现即可。

### 场景

ServletContextListener：监听服务器启动停止

HttpSessionBindingListener：监听某个对象绑定到session域中