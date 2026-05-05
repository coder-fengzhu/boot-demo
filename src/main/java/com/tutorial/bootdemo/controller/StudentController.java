package com.tutorial.bootdemo.controller;


import com.tutorial.bootdemo.Response;
import com.tutorial.bootdemo.dao.Student;
import com.tutorial.bootdemo.dto.StudentDTO;
import com.tutorial.bootdemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/student/{id}")
    public Response<StudentDTO> getStudentById(@PathVariable long id) {
        log.info("GET /student/{} - fetching student", id);
        StudentDTO result = studentService.getStudentById(id);
        log.info("GET /student/{} - success: {}", id, result);
        return Response.newSuccess(result);
    }

    @GetMapping("/student")
    public Response<List<StudentDTO>> getStudentByAge(@RequestParam int maxAge, @RequestParam int minAge) {
        log.info("GET /student?maxAge={}&minAge={} - querying students by age", maxAge, minAge);
        List<StudentDTO> result = studentService.getStudentsByAges(maxAge, minAge);
        log.info("GET /student - success, found {} students", result.size());
        return Response.newSuccess(result);
    }


    @PostMapping("/student")
    public Response<Long> addNewStudent(@RequestBody StudentDTO studentDTO) {
        log.info("POST /student - adding new student: {}", studentDTO);
        Long id = studentService.addNewStudent(studentDTO);
        log.info("POST /student - success, created student id: {}", id);
        return Response.newSuccess(id);
    }

    @DeleteMapping("/student/{id}")
    public void deleteStudentById(@PathVariable long id) {
        log.info("DELETE /student/{} - deleting student", id);
        studentService.deleteStudentById(id);
        log.info("DELETE /student/{} - successfully deleted", id);
    }

    @PutMapping("/student/{id}")
    public Response<StudentDTO> updateStudentById(@PathVariable long id, @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String email) {
        log.info("PUT /student/{} - updating student, name='{}', email='{}'", id, name, email);
        StudentDTO result = studentService.updateStudentById(id, name, email);
        log.info("PUT /student/{} - success: {}", id, result);
        return Response.newSuccess(result);
    }
}
