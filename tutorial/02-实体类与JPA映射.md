# 第二课：实体类与 JPA 映射

## 学习目标

- 理解什么是 JPA 实体类
- 掌握 `@Entity`、`@Table`、`@Column` 等注解
- 学会用 Java 类描述数据库表结构

---

## 2.1 什么是 JPA？

**JPA (Java Persistence API)** 是 Java 官方提供的 ORM（对象关系映射）规范。

**ORM 理解**：
- 数据库是表（行和列）
- Java 是对象（属性和方法）
- JPA 负责把两者对应起来

```
数据库表 row  ←──对应──→  Java 对象
┌──────────────────┐
│ id | name | age  │    Student
│ 1  | 张三 | 20   │ →  student.id = 1
└──────────────────┘                    student.name = "张三"
```

---

## 2.2 实体类 Student

```java
// dao/Student.java
package com.tutorial.bootdemo.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity                          // 声明这是一个 JPA 实体类
@Table(name = "student")         // 对应数据库表名
@NoArgsConstructor               // Lombok: 生成无参构造
@AllArgsConstructor              // Lombok: 生成全参构造
@Builder                         // Lombok: 生成建造者模式
@Data                            // Lombok: 生成 get/set/toString/equals/hashCode
public class Student {

    @Id                         // 主键
    @Column(name = "id")         // 列名映射
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;
}
```

---

## 2.3 注解详解

### @Entity
告诉 JPA：这个类是一个实体类，需要映射到数据库表。

### @Table(name = "student")
指定对应的数据库表名。如果省略，默认为类名 `Student`。

### @Id + @GeneratedValue

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;
```

- `@Id`：标记主键字段
- `@GeneratedValue`：主键生成策略
  - `IDENTITY`：自增（MySQL AUTO_INCREMENT）
  - `SEQUENCE`：序列（Oracle/PostgreSQL）
  - `TABLE`：序列表（跨数据库）

### @Column

| 属性 | 说明 | 示例 |
|------|------|------|
| name | 数据库列名 | `@Column(name = "email")` |
| nullable | 是否可为空 | `nullable = false` |
| length | 字符串长度 | `length = 100` |

---

## 2.4 Lombok 注解

Lombok 通过注解自动生成代码，减少样板代码。

| 注解 | 生成内容 |
|------|----------|
| `@Data` | getXxx/setXxx/toString/equals/hashCode |
| `@Getter` | 所有 get 方法 |
| `@Setter` | 所有 set 方法 |
| `@NoArgsConstructor` | 无参构造 |
| `@AllArgsConstructor` | 全参构造 |
| `@Builder` | 建造者模式 |

```java
// 使用 @Data 后，自动生成：
public long getId() { return id; }
public void setId(long id) { this.id = id; }
// ... 其他 get/set/toString/equals/hashCode
```

---

## 2.5 数据库表结构对应

```sql
CREATE TABLE student (
  id INT AUTO_INCREMENT PRIMARY KEY,  -- @Id + @GeneratedValue
  name VARCHAR(50) NOT NULL,           -- @Column(name = "name")
  email VARCHAR(100) NOT NULL,         -- @Column(name = "email")
  age INT,                             -- @Column(name = "age")
  gender VARCHAR(10)                   -- @Column(name = "gender")
);
```

```
Java 字段     →    数据库列
────────────────────────────
id (long)     →    id (INT AUTO_INCREMENT)
name (String) →    name (VARCHAR)
email         →    email
age (int)     →    age (INT)
gender        →    gender
```

---

## 2.6 完整示例（含 Gender 枚举）

后续课程会讲到，这里先看下结构：

```java
// 实体类中使用枚举
@Column(name = "gender")
@Convert(converter = GenderConverter.class)
private Gender gender;
```

---

## 本课小结

```
┌─────────────────────────────────────────────┐
│              JPA 实体映射                     │
├─────────────────────────────────────────────┤
│  @Entity        → 标记为实体类                │
│  @Table         → 映射到哪个表                 │
│  @Id            → 主键                        │
│  @GeneratedValue→ 主键生成策略                │
│  @Column        → 列映射                      │
│  Lombok @Data   → 自动生成 get/set            │
└─────────────────────────────────────────────┘
```

---

## 下一课预告

[第三课：Repository 数据访问层](./03-Repository数据访问层.md) - 学会用接口操作数据库
