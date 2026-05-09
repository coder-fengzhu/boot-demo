# 第四课：DTO 与 Converter

## 学习目标

- 理解 DTO（Data Transfer Object）的概念
- 掌握 Entity 与 DTO 的转换
- 学会使用 JPA 属性转换器

---

## 4.1 为什么要用 DTO？

**Entity（实体类）**：对应数据库表结构
**DTO（数据传输对象）**：用于 API 接口传输

```
请求 → Controller → Service → Repository → Database
                    ↓
              Entity ← → DTO
```

### 问题：为什么不直接返回 Entity？

1. **安全性**：Entity 可能包含敏感字段，不应暴露给前端
2. **灵活性**：API 返回结构可能与数据库结构不一致
3. **冗余**：Entity 可能有很多字段，API 只需返回部分
4. **版本**：前后端解耦，各自独立演进

### 示例对比

```java
// Entity（数据库结构）
public class Student {
    private long id;
    private String name;
    private String email;
    private int age;
    private Gender gender;
    private Date createTime;  // 数据库有，但 API 不需要
    private Date updateTime;   // 数据库有，但 API 不需要
}

// DTO（API 传输结构）
public class StudentDTO {
    private long id;
    private String name;
    private String email;
    private Gender gender;
    // 只有前端需要的字段
}
```

---

## 4.2 DTO 类定义

```java
// dto/StudentDTO.java
package com.tutorial.bootdemo.dto;

import com.tutorial.bootdemo.enums.Gender;
import lombok.*;

@Builder        // 建造者模式
@Data           // get/set/toString/equals/hashCode
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private long id;
    private String name;
    private String email;
    private Gender gender;
    private int minAge;
    private int maxAge;
}
```

---

## 4.3 Converter 转换器

Converter 负责 Entity ↔ DTO 双向转换。

```java
// converter/StudentConverter.java
package com.tutorial.bootdemo.converter;

import com.tutorial.bootdemo.dao.Student;
import com.tutorial.bootdemo.dto.StudentDTO;

public class StudentConverter {

    // Entity → DTO（查询时用）
    public static StudentDTO convertStudent(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setGender(student.getGender());
        return dto;
    }

    // DTO → Entity（新增/更新时用）
    public static Student convertStudent(StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }
}
```

### 使用场景

```java
// Service 层使用
StudentDTO dto = StudentConverter.convertStudent(student);  // 查询结果转换
Student entity = StudentConverter.convertStudent(dto);     // 新增时转换
```

---

## 4.4 JPA Attribute Converter

对于枚举类型，需要告诉 JPA 如何存入数据库。

### Gender 枚举

```java
// enums/Gender.java
package com.tutorial.bootdemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    BOY("boy", "1"),   // 数据库存 "1"
    GIRL("girl", "2"); // 数据库存 "2"

    private String detail;
    private String dbValue;

    public static Optional<Gender> getGenderByValue(String dbValue) {
        return Arrays.stream(Gender.values())
                .filter(g -> g.dbValue.equals(dbValue))
                .findFirst();
    }
}
```

### GenderConverter

```java
// converter/GenderConverter.java
package com.tutorial.bootdemo.converter;

import com.tutorial.bootdemo.enums.Gender;
import jakarta.persistence.*;

@Converter  // JPA 转换器注解
public class GenderConverter implements AttributeConverter<Gender, String> {

    // Java → 数据库：存 "1" 或 "2"
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute.getDbValue();
    }

    // 数据库 → Java：读 "1" 转 BOY，读 "2" 转 GIRL
    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return Gender.getGenderByValue(dbData).orElse(Gender.BOY);
    }
}
```

### 在 Entity 中使用

```java
@Column(name = "gender")
@Convert(converter = GenderConverter.class)
private Gender gender;
```

### 转换流程

```
新增：Gender.BOY → GenderConverter.convertToDatabaseColumn() → 数据库存储 "1"
查询：数据库 "1" → GenderConverter.convertToEntityAttribute() → Gender.BOY
```

---

## 4.5 完整数据流

```
┌──────────────────────────────────────────────────────────────────┐
│                         新增学生流程                               │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. 前端 POST /student 发送 JSON                                 │
│     {"name":"张三", "email":"zhangsan@test.com", "gender":"boy"} │
│                            ↓                                     │
│  2. Controller 接收 StudentDTO（JSON → 对象）                    │
│                            ↓                                     │
│  3. Service 调用 StudentConverter.convertStudent(dto)           │
│                            ↓                                     │
│  4. 得到 Student 实体（DTO → Entity）                            │
│                            ↓                                     │
│  5. Repository.save(student) 存入数据库                         │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                         查询学生流程                               │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. Repository.findById(1) 返回 Student 实体                     │
│                            ↓                                     │
│  2. Service 调用 StudentConverter.convertStudent(entity)        │
│                            ↓                                     │
│  3. 得到 StudentDTO（Entity → DTO）                             │
│                            ↓                                     │
│  4. Controller 返回 JSON（对象 → JSON）                         │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## 本课小结

```
┌───────────────────────────────────────────────────────┐
│                    DTO 与 Converter                    │
├───────────────────────────────────────────────────────┤
│  DTO（Data Transfer Object）                          │
│  → API 传输专用，不暴露数据库结构                       │
│                                                       │
│  Converter 转换器                                      │
│  → StudentConverter: Entity ↔ DTO                    │
│  → GenderConverter: Java enum ↔ 数据库 String         │
│                                                       │
│  @Convert(converter = GenderConverter.class)          │
│  → 在 Entity 字段上标注 JPA 如何转换                   │
└───────────────────────────────────────────────────────┘
```

---

## 下一课预告

[第五课：Service 业务逻辑层](./05-Service业务逻辑层.md) - 编写业务逻辑，处理核心功能
