# SpringBoot引入

## 一、简介

简化Spring应用开发的一个框架

整个Spring技术栈的一个大整合

J2EE开发的一站式解决方案

## 二、微服务

微服务：架构风格

一个应用应该是一组小型服务的组合；可以通过HTTP的方式进行沟通。

单体应用：All in One

![image-20200804105134673](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804105134673.png)

![image-20200804105341165](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804105341165.png)

每一个功能元素最终都是一个可独立替换和独立升级的软件单元

![image-20200804105729518](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804105729518.png)

## 三、SpringBoot HelloWorld

### Spring Initializr

通过创建向导，spring会给我们返回特定版本，特定依赖的maven工程

### 主程序

通过@SpringBootApplication的注解，我们可以指定工程的主程序，通过简单的main方法启动，调用SpringApplication的run方法，传入主程序的运行时类对象和参数集，就可以运行起spring工程，它内嵌了tomcat服务器，通过maven解决依赖问题，省去了我们繁琐的配置过程

### 编写相关的Controller、Service等

和SpringMVC一样，我们可以直接注解Controller、Service等，实现工程

### 部署简化

通过maven中我们引入的build插件，我们可以把工程打包成一个jar，通过简单的运行就可以跑起工程。

## 四、Hello World探究

### Pom.xml

#### 1、父项目

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.2.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

此父项目还依赖于一个父项目

```xml
<parent>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.3.2.RELEASE</version>
    <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
```

dependencies中的properties标签规定了项目要用到的所有插件的版本，真正管理所有的依赖，版本仲裁中心。

以后我们导入的依赖默认是不需要写版本的，除了那些没有在仲裁中心管理的插件。

#### 2、导入的依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

spring-boot-starter  和   -web：

spring-boot-starter：spring-boot场景启动器，帮我们导入了web模块正常运行所依赖的组件



SpringBoot将所有的功能场景都抽取出来，做成一个个的starters，只需要在项目里面引入这些starter相关场景，就会把相应的依赖全部导入进来。

### 主程序类，主入口类

```java
package com.atguigu.springbootdemohelloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDemoHelloworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoHelloworldApplication.class, args);
    }

}
```

@SpringBootApplication:Spring Boot应用标注在某个类上，说明这个类是SpringBoot的主配置类，SpringBoot就应该运行这个类的main来启动SB应用。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
      @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
```

@SpringBootConfiguration：SpringBoot的配置类

​		标注在某个类商，表示这是一个SpringBoot的配置类

​		点进去发现它持有一个@Configuration

​		@Configuration:经典的Spring注解驱动开发的配置注解，在配置类上标注这个注解

​			配置类 --- 配置文件；

​		再点进去，发现它注解为@Component

​		配置类也是容器中的一个组件

**@EnableAutoConfiguration：**开启自动配置功能

​	EnableXXX：利用import注解导入定义，在容器刷新的时候就会自动创建组件 

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
```

- **@AutoConfigurationPackage:**自动配置包

  - ```java
    @Import(AutoConfigurationPackages.Registrar.class)
    public @interface AutoConfigurationPackage {
    ```

  - 它也是利用import注解导入组件

  - **这个注册器会将主配置类(@SpringBootApplication)的所在包以及下面的所有子包里面的所有组件扫描到spring容器（通过注解元信息**

- **@Import(AutoConfigurationImportSelector.class)**:导入哪些组件的选择器

  - 将所有需要导入的组件以全类名的方式返回，这些组件就会被添加到容器中。

  - ```java
    protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
       if (!isEnabled(annotationMetadata)) {
          return EMPTY_ENTRY;
       }
       AnnotationAttributes attributes = getAttributes(annotationMetadata);
       List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
       configurations = removeDuplicates(configurations);
       Set<String> exclusions = getExclusions(annotationMetadata, attributes);
       checkExcludedClasses(configurations, exclusions);
       configurations.removeAll(exclusions);
       configurations = getConfigurationClassFilter().filter(configurations);
       fireAutoConfigurationImportEvents(configurations, exclusions);
       return new AutoConfigurationEntry(configurations, exclusions);
    }
    ```

  - configurations中含有所有需要导入的组件的全类名，他们是一个个的自动配置类（xxxAutoConfiguration），就是给容器中导入这个场景需要的所有组件，并配置好这些组件

  - ![image-20200804132631970](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804132631970.png)

  - 免去了我们手动编写配置注入功能组件的工作

    - 如何获得自动配置类？
    - SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class,getBeanClassLoader);
    - 从类路径下META-INF/spring.factories中获取EnableAutoConfiguration指定的值
      - 在导入的各个jar包下，找到对应的值导入到容器中，自动配置类就生效了。
      - 我们在maven中导入了autoconfigure的jar包，其中包含有功能组件的全类名信息
    - 这是J2EE技术栈的大整合！整体解决方案和自动配置！

### 创建向导探究

默认生成SpringBoot的项目：

- 主程序和依赖配置的pom已经生成好了，我们只需要关注自己的逻辑
- resources文件夹探究
  - static：保存所有的静态资源；js css images；
  - templates：保存所有的模板页面；（SpringBoot默认jar包使用embed的Tomcat，默认不支持JSP页面）；可以使用模板引擎来达到支持（freemarker、thymeleaf
  - application.properties：SpringBoot应用的配置文件；可以修改一些默认设置

# SpringBoot配置

## 配置文件——application.properties

SpringBoot使用一个全局的配置文件，名称固定

- application.properties
- application.yml

配置文件的作用：修改SpringBoot自动配置的默认值

SpringBoot在底层都给我们自动配置好；我们的自定义要以配置文件形式进行

标记语言：

​	以前的配置文件，大多使用的是xml

YAML（YAML Ain't Markup Language）

​	既是一个标记语言，又不是一个标记语言

​	它以数据为中心，更适合做配置文件

​	YAML实例:

```yml
server:
  port: 8090
```

​	XML实例：

```xml
<server>
	<port>8090</port>
</server>
```

​	可以看到xml的大量数据浪费在了标签的开闭上。

## YAML语法

### 1、基本语法

k:(空格)v   表示一对键值对（空格必须有

利用空格的缩进来控制层级关系，只要是左对齐的一列数据都是同一个层级的

属性和值也是大小写敏感的；

### 2、值的写法

字面量：普通的值（数字，字符串，布尔）

​	k:v  字面直接来写

​		字符串默认不用加上单引号或者双引号

​		"":双引号封装的内容不会转义字符串里面的特殊字符，特殊字符会作为本身想表示的意思

​		'':会转义特殊字符，最终只是一个普通的字符串数据

对象、Map（属性和值）（键值对）：

​	k: v  在下一行来写对象的属性和值的关系，注意缩进

​		对象还是k: v的形式

```yml
friends:
	lastName: zhangsan
	age: 20
```

行内写法：

```yml
friends: {lastName: zhangsan,age: 18}
```

数组（List、Set）：

用- 值表示数组中的一个元素

```yaml
pets:
 - cat
 - dog
 - pig
```

行内写法

```yaml
pets [cat,dog,pig]
```

## 配置的使用

### 利用yml配置对象

pom.xml

```xml
<!--    导入配置文件处理器，配置文件进行绑定就会有提示    -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

Person.java

```java
/*
映射配置文件中的值到组件
@ConfigurationProperties: 告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定;
prefix: 指定配置文件中哪个键下面的所有属性和本类进行属性配置一一映射

这个组件只有在容器中才能被配置处理器识别
 */
@ConfigurationProperties(prefix = "person")
public class Person {

    private String lastName;
    private Integer age;
    private Boolean isBoss;
    private Date birth;

    private Map<String, Object> maps;
    private List<Object> lists;
    private Dog dog;
```

application.yml

```yml
person:
  lastName: zhangsan
  age: 18
  boss: false
  birth: 2017/12/12
  maps: {k1: v1, k2: 12}
  lists:
    - lisi
    - zhangliu
  dog:
    name: 小狗
    age: 2
```

### @Value获取值和@ConfigurationProperties获取值的比较

|                | @ConfigurationProperties     | @Value                 |
| -------------- | ---------------------------- | ---------------------- |
| 功能           | 批量注入配置文件中的属性     | 一个个指定，用${}      |
| 松散语法       | 支持（- _ 后接的字母表示大写 | 不支持（需要精准匹配） |
| SpEL(#{})      | 不支持                       | 支持                   |
| JSR303数据校验 | 支持                         | 不支持                 |

结论：配置文件yml还是properties都能获取到值

### 配置文件注入值数据校验

```java
@Component
@Validated
@ConfigurationProperties(prefix = "person")
public class Person {

    @Email
    private String Email;
```

结论：如果我们只是需要配置文件的某个值，就用@Value注解注入，否则就用@ConfigurationProperties注入

### @PropertySource和ImportResource

@ConfigurationProperties：默认从全局配置文件中加载值

@PropertySource：加载指定的配置文件

@ImportResource：导入Spring的配置**文件**，让配置文件里面的内容生效；需要把它标注到配置类上



SpringBoot推荐给容器中添加组件的方式：全注解方式

1. 配置类---->相当于配置文件，且不需要ImportResource来注入，会自动识别

```java
@Configuration
public class MyConfig {
    //将方法的返回值添加到容器中，容器中这个组件默认的id就是方法名
    @Bean
    public HelloService helloService02(){
        return new HelloService();
    }
}
```

### 配置文件占位符

![image-20200804155814895](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804155814895.png)

### Profile切换环境配置支持

#### 1、多Profile文件

我们在主配置文件编写的时候，文件名可以是 application-{profile}.properties/yml

默认使用application.properties；可能是.properties优先于.yml

可以在application.properties中通过设置

spring.profiles.active=dev	这样的方式来设置启动的环境，便可以激活指定的配置文件

#### 2、yml文档块

通过三个短横线就可以在一个yml中切割出不同的文档块

例子

```yaml
server:
  port: 8090
spring:
  profiles:
    active: test
---
server:
  port: 8091
spring:
  profiles: dev

---
server:
  port: 8092
spring:
  profiles: prod

---
server:
  port: 8093
spring:
  profiles: test


person:
  lastName: zhangsan
  age: ${random.int(20)}
  boss: false
  birth: 2017/12/12
  maps: {k1: v1, k2: 12}
  lists:
    - ${person.lastName}
    - zhangliu
  dog:
    name: 小狗
    age: 2
```

#### 3、命令行方式

启动参数(Program arguments)中使用--spring.profiles.active=dev

#### 4、虚拟机参数

VM options：-Dspring.profiles.active=dev

### 配置文件位置的加载位置

![image-20200804163514166](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804163514166.png)

SpringBoot会从这四个位置全部加载主配置文件，这所有的配置文件的内容形成**相同的内容覆盖，不同的进行补充**的加载原则



==我们还可以通过spring.config.location来改变默认的配置文件位置==

**项目打包好以后，我们可以使用命令行参数的形式，启动项目的时候来指定配置文件的新位置；指定配置文件和默认加载的这些配置文件共同起作用形成互补配置。**且指定的配置文件优先级最高。

这样的技巧在需要修改少量参数的运维环境特别有用。

#### 外部配置的加载顺序

**SpringBoot也从以下位置加载配置，优先级从高到低~**

==由jar包外向jar包内加载；且优先加载带有profile的==

![image-20200804170015872](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804170015872.png)

## 自动配置的原理

配置文件能配置的属性参照

**自动配置源码解析：**

1. SpringBoot启动时，@EnableAutoConfiguration

2. @EnableAutoConfiguration:

   1. 利用EnableAutoConfigurationImportSelector给容器中导入一些组件

   2. 关键在于selectImports方法中的

      ```java
      List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
      ```

   3. 此方法的作用是获取候选的配置

      ```java
      List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());
      //扫描所有jar包类路径下META-INF/spring.factories
      //把扫描到的这些文件的内容包装成properties对象，
      //从properties中获取到EnableAutoConfiguration.class类对应的值，把它们添加到容器中
      ```

   4. 结果：

      ```xml
      org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
      org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
      org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
      org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
      org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
      org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
      org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
      org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration,\
      org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration,\
      org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration,\
      org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration,\
      org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.r2dbc.R2dbcTransactionManagerAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration,\
      org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration,\
      org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration,\
      org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,\
      org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration,\
      org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration,\
      org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration,\
      org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration,\
      org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration,\
      org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration,\
      org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration,\
      org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration,\
      org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration,\
      org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration,\
      org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration,\
      org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration,\
      org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration,\
      org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
      org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,\
      org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration,\
      org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration,\
      org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,\
      org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration,\
      org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration,\
      org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration,\
      org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration,\
      org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration,\
      org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration,\
      org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration,\
      org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration,\
      org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,\
      org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration,\
      org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration,\
      org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration,\
      org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration,\
      org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration,\
      org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration,\
      org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration,\
      org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,\
      org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration,\
      org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration,\
      org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,\
      org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration,\
      org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration,\
      org.springframework.boot.autoconfigure.rsocket.RSocketMessagingAutoConfiguration,\
      org.springframework.boot.autoconfigure.rsocket.RSocketRequesterAutoConfiguration,\
      org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration,\
      org.springframework.boot.autoconfigure.rsocket.RSocketStrategiesAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.rsocket.RSocketSecurityAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration,\
      org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration,\
      org.springframework.boot.autoconfigure.session.SessionAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration,\
      org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration,\
      org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration,\
      org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration,\
      org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration,\
      org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration,\
      org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration,\
      org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration,\
      org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration,\
      org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
      org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration,\
      org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration,\
      org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration,\
      org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration,\
      org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration
      ```

   5. 这样的每一个xxxAutoConfiguration类都是容器中的一个组件，都加入到容器中，用他们来做自动配置

   6. 接下来，每一个自动配置类都可以执行配置功能

   7. 以HttpEncodingAutoConfiguration为例子查看配置过程

      HttpEncodingAutoConfiguration.java

      ```java
      @Configuration(proxyBeanMethods = false)//表示这是一个配置类，可以添加组件
      @EnableConfigurationProperties(ServerProperties.class)
      //启用指定类的ConfigurationProperties功能，将配置文件中对应的值和HttpEncodingProperties绑定起来
      @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
      //Spring底层@Conditional注解，根据不同的条件，如果满足，整个配置类里面的配置就会生效
      //这里就是判断当前应用是否是Web应用
      @ConditionalOnClass(CharacterEncodingFilter.class)
      //判断当前项目有没有指定的类
      //这个类是SpringMVC中进行乱码解决的过滤器
      @ConditionalOnProperty(prefix = "server.servlet.encoding", value = "enabled", matchIfMissing = true)
      //判断配置文件中是否存在某个配置	
      //即使不配置，也是默认生效(matchIfMissing)
      public class HttpEncodingAutoConfiguration {
          //它已经和SpringBoot的配置文件映射了
          private final Encoding properties;
      	//只有一个有参构造器的情况下，参数的值就会从容器中拿到
      	public HttpEncodingAutoConfiguration(ServerProperties properties) {
      		this.properties = properties.getServlet().getEncoding();
      	}
          @Bean //给容器中添加一个组件，这个组件的某个只需要从properties中获取
      	@ConditionalOnMissingBean //
      	public CharacterEncodingFilter characterEncodingFilter() {
      		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
      		filter.setEncoding(this.properties.getCharset().name());
      	filter.setForceRequestEncoding(this.properties.shouldForce(Encoding.Type.REQUEST));
      		filter.setForceResponseEncoding(this.properties.shouldForce(Encoding.Type.RESPONSE));
      		return filter;
      	}
      ```

      根据当前的不同条件判断此配置类是否工作

      一旦配置类生效，这个配置类就会给容器中添加各种组件，而这些组件的属性是从对应的properties类中获取的，这些类里面的每一个属性又是和配置文件绑定的。

      ServerProperties.class

      ```java
      @ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
      public class ServerProperties {
      ```

      此注解从配置文件中引入指定的值和bean的属性进行绑定

      **精髓：**

      - **SpringBoot启动会加载大量的自动配置类**
      - **我们看我们需要的功能有没有SpringBoot默认写好的自动配置类**
      - **我们再来看这个自动配置类中到底配置了哪些组件；要用的组件有，我们就不需要再来配置了。**
      - **没有的话就需要自己编写配置类注入**
      - **给容器中自动配置类添加组件的时候，huicongproperties类中获取某些属性，我们就可以在配置文件中指定这些属性的值。**

      xxxAutoConfiguration:自动配置类

      给容器中添加组件

      xxxProperties：封装配置文件中相关属性

**细节：**

1、@Conditional派生注解（Spring注解版原生的@Conditional作用）

作用：必须是Conditional指定的条件成立，才给容器中添加组件，配置文件里面的所有内容才生效

![image-20200804182650857](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804182650857.png)

自动配置类必须在一定的条件下才能生效；

我们怎么知道哪些自动配置类生效呢？

我们可以在配置文件中开启debug

**debug=true**

这样就可以在控制台中看到配置的开启和关闭相关通知信息（**Positive matches/Negative matches**）

# SpringBoot与日志

## 1、日志框架

在统一的日志门面（接口层）下，具体去实现功能（类似于JDBC-数据库驱动这样的关系）

市面上的日志框架：

JUL、JCL、Jboss-logging、logback、log4j、log4j2、slf4j…

| 日志门面（日志抽象                                           | 日志实现                                       |
| ------------------------------------------------------------ | ---------------------------------------------- |
| ~~JCL(Jakarta Commons Logging)~~、SLF4j(Simple Logging Facade forJava)、~~jboss-logging~~ | Log4j、JUL(java.util.logging)、Log4j2、Logback |

左边选一个门面，右边选一个实现

日志门面：SLF4j

日志实现：Logback

SpringBoot：底层的Spring框架，默认使用JCL

​	SpringBoot选用：==SLF4j和Logback==

## 2、SLF4j使用

### 1、如何在系统中使用SLF4j

以后开发的时候，日志记录方法的调用；不应该来直接调用日志的实现类，而是调用日志抽象层面的方法（抽象不依赖于实现

我们需要导入slf4j的jar和logback的实现jar

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```

### ![click to enlarge](C:\Users\q1367\Desktop\SpringBoot\images\concrete-bindings.png)

每一个日志的实现框架都有自己的配置文件。使用slf4j以后，**配置文件还是做成日志实现框架的配置文件。**

### 2、遗留问题

A系统(slf4j+logback)：有很多框架，而且每个框架还有自己的日志系统，运用了其他的日志门面

这就需要我们统一日志门面：

![click to enlarge](C:\Users\q1367\Desktop\SpringBoot\images\legacy.png)

**如何统一系统中所有日志？**

==1、将系统中其他日志框架先排除出去；==

==2、用中间包来替换原有的日志框架==

==3、我们导入slf4j其他的实现==

## 3、SpringBoot日志关系

最基本的依赖:spring-boot-starter

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter</artifactId>
  <version>2.3.2.RELEASE</version>
  <scope>compile</scope>
</dependency>
```

spring-boot-starter中依赖：spring-boot-starter-logging

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-logging</artifactId>
  <version>2.3.2.RELEASE</version>
  <scope>compile</scope>
</dependency>
```

SpringBoot日志底层依赖关系

![image-20200804191311826](C:\Users\q1367\Desktop\SpringBoot\images\image-20200804191311826.png)

总结：

1. SpringBoot底层也是使用slf4j+logback的方式进行日志记录

2. 同时解决了其他框架使用其他日志依赖的冲突关系，统一日志门面

3. 如果我们要引入其他框架，一定要把这个框架的默认日志依赖移除掉

   ​	Spring框架默认的commons-loggings怎么样了？

   ​	1.5版本的时候是声明对commons-logging的依赖排除，2.3不清楚

## 4、日志使用

### 1、默认配置

SpringBoot默认帮我们配置好了日志

测试类：

```java
@SpringBootTest
class SpringbootDemo03LoggingApplicationTests {
    //记录器
    Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    void contextLoads() {
        //日志的级别：
        //由低到高：trace、debug、info、warn、error
        //可以调整需要输出的日志级别：日志就只会在这个级别以及以后的高级别生效
        logger.trace("这是跟踪信息，trace日志");
        logger.debug("这是debug日志");
        //springboot 默认给我们使用info以及以后的级别
        //调整在配置文件中进行
        logger.info("这是info日志");
        logger.warn("这是warn日志");
        logger.error("这是error日志");
    }
}
```

application.properties：

```properties
#logging.level需要我们出传入一个map，把包名或者全类名当做键，就可以用值规定其日志级别；
#否则就用默认的：root级别
logging.level.com.atguigu=debug
```

**其他的配置项：**

logging.file/logging.path

指定了file，就是指定日志输出到指定文件名的日志文件，可以制定完整的路径

指定了path，就是指定日志输出到的日志文件路径，默认文件名是spring.log文件

这两个是冲突设置，都指定是file生效。

logging.pattern.console

表示在控制台输出的日志格式

logging.pattern.file

指定文件中日志输出的格式

### 2、指定配置

给类路径下放上每个日志框架自己的配置文件即可；SpringBoot就不使用默认配置了；

规则是用指定的xml命名，且不同的命名有加载优先级的区别

eg：logback.xml：直接就被日志框架识别了

logback-spring.xml：日志框架就不直接加载日志的配置项，而由SpringBoot加载

​	这样就可以得到SpringBoot框架的扩展支持，高级特性

```xml
<springProfile name="staging">
    <!-- Configuration to be enabled when the "staging" profile is active -->
	可以指定某段配置只在某个环境下生效
    <pattern>指定的日志格式</pattern>
</springProfile>
```

## 5、切换日志框架

直接操作maven的依赖树，exclusion老的框架， 添加新的框架包（一般框架官方手册都有legacy API的兼容说明，按照其依赖逻辑操作即可）

# SpringBoot与Web开发

使用SpringBoot

1. 使用SpringBoot应用，选择我们需要的模块
2. SpringBoot已经默认配置好了这些场景（AutoConfiguration，只需要在配置文件中指定少量配置就可以运行起来
3. 自己编写业务代码

**关键：自动配置原理**

关心每个场景SpringBoot做了什么配置，能做什么修改？修改方式是什么？能否拓展?……

检查每个jar的xxxAutoConfiguration

配套的EnableConfigurationProperties，会注册一个个的xxxProperties类

配置类封装配置文件的内容