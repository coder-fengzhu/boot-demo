# Spring Boot 学生管理系统教程

## 项目概述

这是一个基于 Spring Boot 3.2.5 的学生管理系统演示项目，展示了标准的 Spring Boot Web 应用分层架构。

**教程视频**: https://www.bilibili.com/video/BV1gm411m7i6/

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 3.2.5 | 核心框架 |
| Spring Data JPA | 数据访问层 |
| MySQL | 数据库 |
| Lombok | 简化 POJO 代码 |
| Maven | 项目构建 |

## 项目结构

```
com.tutorial.bootdemo
├── BootDemoApplication.java    # Spring Boot 启动类
├── Response.java               # 统一响应封装类
├── controller/
│   └── StudentController.java  # REST 控制器
├── service/
│   ├── StudentService.java     # 服务接口
│   └── StudentServiceImpl.java  # 服务实现
├── dao/
│   ├── Student.java            # JPA 实体类
│   └── StudentRepository.java  # 数据访问层
├── dto/
│   └── StudentDTO.java         # 数据传输对象
├── enums/
│   └── Gender.java             # 性别枚举
└── converter/
    ├── GenderConverter.java    # JPA 属性转换器
    └── StudentConverter.java   # DTO 转换器
```

## 核心概念

### 1. 分层架构

```
┌─────────────────┐
│   Controller    │  处理 HTTP 请求/响应
├─────────────────┤
│    Service      │  业务逻辑处理
├─────────────────┤
│ Repository/DAO  │  数据访问层
├─────────────────┤
│     Entity      │  数据库表映射
└─────────────────┘
```

### 2. RESTful API 设计

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/student/{id}` | 根据 ID 查询学生 |
| GET | `/student?minAge=X&maxAge=Y` | 区间查询学生 |
| POST | `/student` | 新增学生 |
| PUT | `/student/{id}` | 更新学生信息 |
| DELETE | `/student/{id}` | 删除学生 |

### 3. 统一响应格式

```json
{
  "success": true,
  "data": { ... },
  "errorMsg": null
}
```

### 4. DTO 与 Entity 转换

- **Entity (Student)**: 与数据库表一一对应
- **DTO (StudentDTO)**: 用于 API 传输，不暴露数据库结构
- **Converter**: 负责两者之间的转换

### 5. JPA Attribute Converter

`GenderConverter` 负责将 Java 枚举 `Gender` 存储为数据库字符串：

```java
BOY("boy", "1")  →  数据库存储 "1"
GIRL("girl", "2") → 数据库存储 "2"
```

## 数据库配置

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=
```

## 建库建表 SQL

```sql
CREATE DATABASE test CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE student (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  age INT,
  gender VARCHAR(10)
);
```

## 运行项目

```bash
# 编译打包
./mvnw clean package

# 运行
./mvnw spring-boot:run
# 或
java -jar target/boot-demo-0.0.1-SNAPSHOT.jar
```

## API 使用示例

### 新增学生
```bash
curl -X POST http://localhost:8080/student \
  -H "Content-Type: application/json" \
  -d '{"name":"张三","email":"zhangsan@example.com","age":20}'
```

### 查询学生
```bash
curl http://localhost:8080/student/1
```

### 区间查询
```bash
curl "http://localhost:8080/student?minAge=18&maxAge=25"
```

### 更新学生
```bash
curl -X PUT "http://localhost:8080/student/1?name=李四&email=lisi@example.com"
```

### 删除学生
```bash
curl -X DELETE http://localhost:8080/student/1
```

## 关键注解说明

| 注解 | 位置 | 作用 |
|------|------|------|
| `@SpringBootApplication` | 启动类 | 标识 Spring Boot 应用 |
| `@RestController` | Controller | RESTful 控制器 |
| `@Service` | ServiceImpl | 业务层组件 |
| `@Repository` | Repository | 数据访问层组件 |
| `@Entity` | Student | JPA 实体类 |
| `@Autowired` | 构造函数/字段 | 依赖注入 |
| `@Transactional` | Service 方法 | 事务管理 |
