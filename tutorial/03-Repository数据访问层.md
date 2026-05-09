# 第三课：Repository 数据访问层

## 学习目标

- 理解 Repository 的作用
- 掌握 JpaRepository 的用法
- 学会使用派生查询方法

---

## 3.1 什么是 Repository？

Repository 是数据访问层，负责与数据库交互。可以理解为 DAO（Data Access Object）。

**三层架构中的位置**：

```
┌─────────────────┐
│   Controller    │  接收请求
├─────────────────┤
│    Service      │  处理业务
├─────────────────┤
│  Repository     │  访问数据  ← 本课内容
├─────────────────┤
│     MySQL       │  存储数据
└─────────────────┘
```

---

## 3.2 JpaRepository

Spring Data JPA 提供了强大的 `JpaRepository`，只需继承接口，无需编写实现。

```java
// dao/StudentRepository.java
package com.tutorial.bootdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // 无需编写任何代码！
}
```

### 继承 JpaRepository<实体类型, 主键类型>

| 类型参数 | 本项目 |
|----------|--------|
| Student | 实体类名 |
| Long | 主键 id 的类型 |

### 自动获得的方法

```java
studentRepository.save(student);           // 保存/更新
studentRepository.findById(1L);           // 根据 ID 查询
studentRepository.findAll();              // 查询所有
studentRepository.deleteById(1L);         // 根据 ID 删除
studentRepository.count();                // 统计数量
studentRepository.existsById(1L);         // 判断是否存在
```

---

## 3.3 派生查询方法

JpaRepository 支持根据方法名自动生成 SQL 查询。

### 规则：findBy + 字段名

```java
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 根据邮箱查询 → SELECT * FROM student WHERE email = ?
    List<Student> findByEmail(String email);

    // 根据年龄区间查询 → SELECT * FROM student WHERE age BETWEEN ? AND ?
    List<Student> findByAgeBetween(int minAge, int maxAge);

    // 根据姓名模糊查询（以 XX 开头）→ SELECT * FROM student WHERE name LIKE 'XX%'
    List<Student> findByNameStartingWith(String namePrefix);
}
```

### 常用关键字

| 关键字 | 示例 | 生成的 SQL |
|--------|------|-----------|
| `findBy` | `findByEmail` | `WHERE email = ?` |
| `findByAgeBetween` | | `WHERE age BETWEEN ? AND ?` |
| `findByNameStartingWith` | | `WHERE name LIKE '?%'` |
| `findByNameContaining` | | `WHERE name LIKE '%?%'` |
| `findByAgeGreaterThan` | | `WHERE age > ?` |
| `findByAgeLessThan` | | `WHERE age < ?` |
| `findByNameAndEmail` | | `WHERE name = ? AND email = ?` |
| `findByNameOrEmail` | | `WHERE name = ? OR email = ?` |
| `findByIdIn` | | `WHERE id IN (?, ?, ...)` |

---

## 3.4 自定义查询 @Query

对于复杂查询，可以使用 `@Query` 注解写 JPQL 或原生 SQL。

```java
@Query(value = "select new com.tutorial.bootdemo.dao.Student(name, email) from Student where email = :email")
List<Student> findByEmail2(@Param("email") String email);
```

| 注解 | 说明 |
|------|------|
| `@Query` | 自定义查询 |
| `:email` | 命名参数 |
| `@Param` | 绑定方法参数到命名参数 |

### JPQL vs 原生 SQL

```java
// JPQL（推荐，跨数据库）
@Query("select s from Student s where s.email = :email")

// 原生 SQL
@Query(value = "SELECT * FROM student WHERE email = :email", nativeQuery = true)
```

---

## 3.5 JpaSpecificationExecutor（进阶）

支持动态条件查询：

```java
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    // 额外获得了 .findAll(Specification) 方法
}
```

---

## 3.6 代码示例

```java
@Service
public class StudentServiceImpl {

    @Autowired
    private StudentRepository studentRepository;

    public void demo() {
        // 保存
        Student s = new Student();
        s.setName("张三");
        s.setEmail("zhangsan@example.com");
        studentRepository.save(s);

        // 根据 ID 查询
        Optional<Student> opt = studentRepository.findById(1L);
        Student student = opt.orElseThrow(() -> new RuntimeException("没找到"));

        // 根据邮箱查询
        List<Student> list = studentRepository.findByEmail("zhangsan@example.com");

        // 年龄区间查询
        List<Student> youngStudents = studentRepository.findByAgeBetween(18, 25);

        // 删除
        studentRepository.deleteById(1L);
    }
}
```

---

## 本课小结

```
┌─────────────────────────────────────────────┐
│           Repository 数据访问层              │
├─────────────────────────────────────────────┤
│  继承 JpaRepository<Student, Long>          │
│  → 自动获得：save, findById, findAll, delete │
│                                           │
│  派生查询：findBy + 字段名                   │
│  → findByEmail, findByAgeBetween            │
│                                           │
│  @Query 注解自定义复杂查询                    │
└─────────────────────────────────────────────┘
```

---

## 下一课预告

[第四课：DTO 与 Converter](./04-DTO与Converter.md) - 学会数据转换，分离内部结构与外部接口
