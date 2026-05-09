# 第六课：Controller 控制器

## 学习目标

- 理解 Controller 的职责
- 掌握 RESTful API 设计
- 学会处理 HTTP 请求和响应

---

## 6.1 Controller 的职责

Controller（控制器）负责：

1. **接收 HTTP 请求**：解析参数
2. **调用 Service**：处理业务逻辑
3. **返回 HTTP 响应**：封装返回数据

```
浏览器/前端  →  Controller  →  Service  →  Repository  →  数据库
     ↑                                            ↓
     └──────────────  Response JSON ←──────────────┘
```

---

## 6.2 RESTful API 设计

RESTful 是一种 API 设计规范，使用 HTTP 方法表达操作。

### 基本规则

| HTTP 方法 | 语义 | 示例 | 说明 |
|-----------|------|------|------|
| GET | 查询 | `GET /student/1` | 获取资源 |
| POST | 新增 | `POST /student` | 创建资源 |
| PUT | 更新 | `PUT /student/1` | 完整更新 |
| DELETE | 删除 | `DELETE /student/1` | 删除资源 |

### URL 设计

```
# 查询单个
GET    /student/{id}

# 条件查询（多个参数）
GET    /student?minAge=18&maxAge=25

# 新增
POST   /student

# 更新
PUT    /student/{id}

# 删除
DELETE /student/{id}
```

---

## 6.3 @RestController

```java
@RestController  // = @Controller + @ResponseBody
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ...
}
```

### @RestController vs @Controller

| 注解 | 返回值处理 |
|------|-----------|
| `@Controller` | 返回视图（HTML），或配合 `@ResponseBody` 返回数据 |
| `@RestController` | 直接返回数据（JSON/XML），专用于 API |

---

## 6.4 请求映射注解

### @GetMapping / @PostMapping / @PutMapping / @DeleteMapping

```java
@GetMapping("/student/{id}")      // 处理 GET 请求
@PostMapping("/student")          // 处理 POST 请求
@PutMapping("/student/{id}")      // 处理 PUT 请求
@DeleteMapping("/student/{id}")   // 处理 DELETE 请求
```

### @PathVariable：路径参数

```java
@GetMapping("/student/{id}")
public StudentDTO getStudent(@PathVariable long id) {
    // @PathVariable 将 URL 中的 {id} 映射到参数
    return studentService.getStudentById(id);
}

// 请求：GET /student/1
// id = 1
```

### @RequestParam：查询参数

```java
@GetMapping("/student")
public List<StudentDTO> getByAge(
        @RequestParam int minAge,
        @RequestParam int minAge) {
    return studentService.getStudentsByAges(minAge, maxAge);
}

// 请求：GET /student?minAge=18&maxAge=25
```

### @RequestBody：请求体

```java
@PostMapping("/student")
public Response<Long> add(@RequestBody StudentDTO dto) {
    // @RequestBody 将 JSON 映射为对象
    Long id = studentService.addNewStudent(dto);
    return Response.newSuccess(id);
}

// 请求：POST /student
// Body: {"name":"张三","email":"zhangsan@test.com"}
```

---

## 6.5 完整 Controller 代码

```java
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    // GET /student/{id}
    @GetMapping("/student/{id}")
    public Response<StudentDTO> getStudentById(@PathVariable long id) {
        return Response.newSuccess(studentService.getStudentById(id));
    }

    // GET /student?minAge=18&maxAge=25
    @GetMapping("/student")
    public Response<List<StudentDTO>> getStudentByAge(
            @RequestParam int maxAge,
            @RequestParam int minAge) {
        return Response.newSuccess(studentService.getStudentsByAges(maxAge, minAge));
    }

    // POST /student
    @PostMapping("/student")
    public Response<Long> addNewStudent(@RequestBody StudentDTO studentDTO) {
        return Response.newSuccess(studentService.addNewStudent(studentDTO));
    }

    // DELETE /student/{id}
    @DeleteMapping("/student/{id}")
    public void deleteStudentById(@PathVariable long id) {
        studentService.deleteStudentById(id);
    }

    // PUT /student/{id}
    @PutMapping("/student/{id}")
    public Response<StudentDTO> updateStudentById(
            @PathVariable long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        return Response.newSuccess(
            studentService.updateStudentById(id, name, email)
        );
    }
}
```

---

## 6.6 API 调用示例

### 使用 curl 命令测试

```bash
# 新增学生
curl -X POST http://localhost:8080/student \
  -H "Content-Type: application/json" \
  -d '{"name":"张三","email":"zhangsan@test.com"}'

# 查询单个
curl http://localhost:8080/student/1

# 条件查询
curl "http://localhost:8080/student?minAge=18&maxAge=25"

# 更新
curl -X PUT "http://localhost:8080/student/1?name=李四&email=lisi@test.com"

# 删除
curl -X DELETE http://localhost:8080/student/1
```

---

## 本课小结

```
┌─────────────────────────────────────────────────────┐
│              Controller 控制器                        │
├─────────────────────────────────────────────────────┤
│  @RestController：RESTful API 专用                  │
│                                                     │
│  @GetMapping/PostMapping/PutMapping/DeleteMapping    │
│                                                     │
│  @PathVariable：URL 路径参数                         │
│  @RequestParam：查询参数                            │
│  @RequestBody：请求体（JSON → 对象）                 │
└─────────────────────────────────────────────────────┘
```

---

## 下一课预告

[第七课：枚举与统一响应](./07-枚举与统一响应.md) - 完善枚举类型，写好 API 响应格式
