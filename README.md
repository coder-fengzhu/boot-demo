视频教程地址：https://www.bilibili.com/video/BV1gm411m7i6/

## Spring Boot简介
<font style="color:rgb(13, 13, 13);">Spring Boot 是一个基于 Spring 框架的开源框架，用于简化 Spring 应用程序的初始搭建和开发过程。它通过提供约定优于配置的方式，尽可能减少开发者的工作量，使得开发 Spring 应用变得更加快速、便捷和高效。</font>

<font style="color:rgb(13, 13, 13);"></font>

<font style="color:rgb(13, 13, 13);">Spring Boot 的主要特点包括：</font>

1. **简化配置**<font style="color:rgb(13, 13, 13);">: Spring Boot 遵循约定优于配置的原则，减少了传统 Spring 应用中的大量配置。它通过自动配置（auto-configuration）和起步依赖（starter dependencies）来简化项目的配置过程，让开发者可以快速搭建起一个可运行的 Spring 应用。</font>
2. **集成性强**<font style="color:rgb(13, 13, 13);">: Spring Boot 提供了大量的开箱即用的特性和功能，如内嵌的 Servlet 容器（如Tomcat、Jetty或Undertow）、健康检查、指标监控等。它还整合了诸多常用的库和框架，如Spring Data、Spring Security等，使得开发者可以快速构建出功能完善的应用。</font>
3. **微服务支持**<font style="color:rgb(13, 13, 13);">: Spring Boot 非常适合用于构建微服务架构。它提供了丰富的支持，如通过Spring Cloud进行微服务架构的开发，集成了服务发现、配置中心、负载均衡等功能，帮助开发者构建可伸缩、高可用的微服务系统。</font>
4. **内嵌服务器**<font style="color:rgb(13, 13, 13);">: Spring Boot 可以将应用程序打包成一个可执行的 JAR 文件，并内置了常用的 Servlet 容器，如 Tomcat、Jetty 或 Undertow。这样一来，开发者可以通过简单的 </font>**java -jar**<font style="color:rgb(13, 13, 13);"> 命令来运行应用程序，而无需部署到外部应用服务器。</font>
5. **生态丰富**<font style="color:rgb(13, 13, 13);">: 由于 Spring Boot 的广泛应用和强大生态系统，开发者可以轻松地使用各种扩展和插件，如 Actuator、Spring Boot DevTools 等，提高开发效率和应用质量。</font>



## 项目结构
![](https://cdn.nlark.com/yuque/0/2024/png/26411187/1713671184591-1eaa108f-8494-406e-8d36-cc3f69e033e3.png)



![](https://cdn.nlark.com/yuque/0/2024/png/26411187/1713682478000-c27d690a-0eb1-44b4-8631-d3225c842a6a.png)



## rest api规范
### 路径
路径又称"终点"（endpoint），表示API的具体网址。

在RESTful架构中，每个网址代表一种资源（resource），所以网址中不能有动词，只能有名词，而且所用的名词往往与数据库的表格名对应。



### Http 动词
+ GET（SELECT）：从服务器取出资源（一项或多项）。
+ POST（CREATE）：在服务器新建一个资源。
+ PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
+ PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
+ DELETE（DELETE）：从服务器删除资源。



## 创建数据库表
### 建库语句
```sql
CREATE DATABASE test
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
```



### 建表语句
```sql
CREATE TABLE student (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT
);
```
