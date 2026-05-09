# 第五课：Service 业务逻辑层

## 学习目标

- 理解 Service 层的职责
- 掌握接口与实现分离的设计
- 学会依赖注入和事务管理

---

## 5.1 Service 层的职责

Service（业务逻辑层）是整个应用的核心，负责：

1. **处理业务逻辑**：计算、验证、组装
2. **调用多个 Repository**：完成复杂的数据操作
3. **事务管理**：保证数据一致性
4. **异常处理**：业务相关的异常

```
┌─────────────────┐
│   Controller    │  接收请求，参数校验
├─────────────────┤
│    Service       │  处理业务逻辑  ← 本课重点
├─────────────────┤
│  Repository      │  数据访问
├─────────────────┤
│     MySQL        │  存储数据
└─────────────────┘
```

---

## 5.2 接口与实现分离

遵循**面向接口编程**原则：

```java
// service/StudentService.java（接口）
public interface StudentService {
    StudentDTO getStudentById(long id);
    Long addNewStudent(StudentDTO studentDTO);
    void deleteStudentById(long id);
    StudentDTO updateStudentById(long id, String name, String email);
    List<StudentDTO> getStudentsByAges(int maxAge, int minAge);
}
```

```java
// service/StudentServiceImpl.java（实现）
@Service  // 标记为 Spring Bean
public class StudentServiceImpl implements StudentService {

    @Autowired  // 注入 Repository
    private StudentRepository studentRepository;

    // 实现业务逻辑
}
```

### 为什么分开？

| 好处 | 说明 |
|------|------|
| 职责清晰 | 接口定义"做什么"，实现类负责"怎么做" |
| 易于测试 | 可以注入 Mock 对象进行单元测试 |
| 解耦 | 更换实现不影响调用方 |
| 可读性 | 先看接口了解功能，不被实现细节干扰 |

---

## 5.3 依赖注入 @Autowired

```java
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired  // 将 Repository 注入进来
    private StudentRepository studentRepository;

    // 现在可以调用 studentRepository 了
}
```

**原理**：Spring 会自动创建 StudentRepository 实例，赋值给 `studentRepository` 字段。

---

## 5.4 事务管理 @Transactional

在 Service 方法上添加 `@Transactional`，保证方法内的数据库操作在同一个事务中执行。

```java
@Override
@Transactional  // 保证原子性：要么全部成功，要么全部失败
public StudentDTO updateStudentById(long id, String name, String email) {
    // 1. 查询
    Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("学生不存在"));

    // 2. 修改
    if (name != null) student.setName(name);
    if (email != null) student.setEmail(email);

    // 3. 保存
    return studentRepository.save(student);
}
```

### @Transactional 说明

| 特性 | 说明 |
|------|------|
| 默认传播 | REQUIRED（加入当前事务） |
| 回滚条件 | 抛出 RuntimeException 或 Error 时自动回滚 |
| 隔离级别 | 使用数据库默认（MySQL 通常是可重复读） |
| 只读 | `readOnly = true` 允许数据库做优化 |

---

## 5.5 完整 Service 实现

```java
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // 根据 ID 查询
    @Override
    public StudentDTO getStudentById(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        return StudentConverter.convertStudent(student);
    }

    // 新增学生
    @Override
    public Long addNewStudent(StudentDTO dto) {
        // 检查邮箱是否已被占用
        List<Student> existing = studentRepository.findByEmail(dto.getEmail());
        if (!CollectionUtils.isEmpty(existing)) {
            throw new IllegalStateException("邮箱已被使用");
        }
        // 保存
        Student student = studentRepository.save(
            StudentConverter.convertStudent(dto)
        );
        return student.getId();
    }

    // 删除学生
    @Override
    public void deleteStudentById(long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("学生不存在");
        }
        studentRepository.deleteById(id);
    }

    // 更新学生
    @Override
    @Transactional
    public StudentDTO updateStudentById(long id, String name, String email) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在"));

        if (StringUtils.hasLength(name)) student.setName(name);
        if (StringUtils.hasLength(email)) student.setEmail(email);

        Student updated = studentRepository.save(student);
        return StudentConverter.convertStudent(updated);
    }

    // 年龄区间查询
    @Override
    public List<StudentDTO> getStudentsByAges(int maxAge, int minAge) {
        List<Student> list = studentRepository.findByAgeBetween(minAge, maxAge);
        if (CollectionUtils.isEmpty(list)) {
            return List.of();
        }
        return list.stream()
                .map(StudentConverter::convertStudent)
                .collect(Collectors.toList());
    }
}
```

---

## 5.6 常用工具类

```java
// 判断集合是否为空
import org.springframework.util.CollectionUtils;
CollectionUtils.isEmpty(list)  // true 表示 null 或空集合

// 判断字符串是否有内容
import org.springframework.util.StringUtils;
StringUtils.hasLength(str)     // true 表示非 null 且非空

// 可选值处理
studentRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("没找到"))      // 没值时抛异常
    .orElse(defaultStudent)                                  // 没值时返回默认值
```

---

## 本课小结

```
┌─────────────────────────────────────────────────────┐
│                 Service 业务逻辑层                   │
├─────────────────────────────────────────────────────┤
│  接口 + 实现分离：StudentService + StudentServiceImpl │
│                                                     │
│  @Autowired：注入 Repository                          │
│                                                     │
│  @Transactional：事务管理，保证数据一致性              │
│                                                     │
│  业务逻辑：校验 → 调用 Repository → 返回结果          │
└─────────────────────────────────────────────────────┘
```

---

## 下一课预告

[第六课：Controller 控制器](./06-Controller控制器.md) - 学会编写 RESTful API
