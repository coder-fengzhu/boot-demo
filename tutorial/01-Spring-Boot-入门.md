# 第一课：Spring Boot 入门

## 学习目标

- 理解什么是 Spring Boot
- 掌握项目启动类的写法
- 了解 pom.xml 依赖配置

---

## 1.1 什么是 Spring Boot？

Spring Boot 是 Spring 框架的"快捷启动器"，它的核心思想是**约定优于配置**。

传统 Spring 项目需要大量配置文件，而 Spring Boot 会根据你添加的依赖自动配置，开箱即用。

**类比理解**：
- 传统 Spring = 自己组装电脑（需要买各种零件并连接）
- Spring Boot = 购买品牌整机（插上电源就能用）

---

## 1.2 项目入口：启动类

```java
// BootDemoApplication.java
package com.tutorial.bootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootDemoApplication.class, args);
    }
}
```

### 注解解析

| 注解 | 作用 |
|------|------|
| `@SpringBootApplication` | 标识这是一个 Spring Boot 启动类，包含自动配置、组件扫描等功能 |

### 运行原理

```
启动类.main() → SpringApplication.run() → 启动内嵌 Tomcat → 扫描 @Controller/@Service/@Repository → 交给 Spring 管理
```

---

## 1.3 Maven 依赖配置

```xml
<!-- pom.xml -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.5</version>
</parent>

<dependencies>
    <!-- Web 开发必备：包含 Spring MVC 和内嵌 Tomcat -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- 数据库访问：Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- MySQL 驱动 -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok：自动生成 getter/setter/constructor -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.20</version>
    </dependency>
</dependencies>
```

### 依赖说明

| 依赖 | 说明 |
|------|------|
| spring-boot-starter-web | Web 开发核心（Spring MVC + Tomcat） |
| spring-boot-starter-data-jpa | 数据库访问（Spring Data + Hibernate） |
| mysql-connector-j | MySQL 数据库驱动 |
| lombok | 简化 Java 代码（自动生成 get/set 方法） |

---

## 1.4 配置文件

Spring Boot 使用 `application.properties` 或 `application.yml` 放置配置。

```properties
# application.properties
spring.application.name=boot-demo

# 数据库连接
spring.datasource.url=jdbc:mysql://localhost:3306/test?characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=
```

---

## 1.5 运行项目

```bash
# 方式1：Maven 运行
./mvnw spring-boot:run

# 方式2：打包后运行
./mvnw clean package
java -jar target/boot-demo-0.0.1-SNAPSHOT.jar
```

启动成功后访问：`http://localhost:8080`

---

## 本课小结

```
┌─────────────────────────────────────┐
│         Spring Boot 项目             │
├─────────────────────────────────────┤
│  1. 启动类：@SpringBootApplication   │
│  2. 依赖：starter-web + starter-data │
│  3. 配置：application.properties     │
│  4. 运行：java -jar 或 mvn spring-boot:run │
└─────────────────────────────────────┘
```

---

## 下一课预告

[第二课：实体类与 JPA 映射](./02-实体类与JPA映射.md) - 学会如何用 Java 类描述数据库表
