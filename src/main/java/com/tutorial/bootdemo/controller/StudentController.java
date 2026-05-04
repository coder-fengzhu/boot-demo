package com.tutorial.bootdemo.controller;


import com.tutorial.bootdemo.Response;
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
        log.info("Querying student by id: {}", id);
        Response<StudentDTO> response = Response.newSuccess(studentService.getStudentById(id));
        log.info("Query student by id: {} success, response: {}", id, response.getData());
        return response;
    }

    @GetMapping("/student")
    public Response<List<StudentDTO>> getStudentByAge(@RequestParam int maxAge, @RequestParam int minAge) {
        log.info("Querying students by age range: minAge={}, maxAge={}", minAge, maxAge);
        Response<List<StudentDTO>> response = Response.newSuccess(studentService.getStudentsByAges(maxAge, minAge));
        log.info("Query students by age range success, found {} students", response.getData().size());
        return response;
    }


    @PostMapping("/student")
    public Response<Long> addNewStudent(@RequestBody StudentDTO studentDTO) {
        log.info("Adding new student: {}", studentDTO);
        Response<Long> response = Response.newSuccess(studentService.addNewStudent(studentDTO));
        log.info("Add new student success, id: {}", response.getData());
        return response;
    }

    @DeleteMapping("/student/{id}")
    public void deleteStudentById(@PathVariable long id) {
        log.info("Deleting student by id: {}", id);
        studentService.deleteStudentById(id);
        log.info("Delete student by id: {} success", id);
    }

    @PutMapping("/student/{id}")
    public Response<StudentDTO> updateStudentById(@PathVariable long id, @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String email) {
        log.info("Updating student id: {}, name: {}, email: {}", id, name, email);
        Response<StudentDTO> response = Response.newSuccess(studentService.updateStudentById(id, name, email));
        log.info("Update student id: {} success, response: {}", id, response.getData());
        return response;
    }
}
