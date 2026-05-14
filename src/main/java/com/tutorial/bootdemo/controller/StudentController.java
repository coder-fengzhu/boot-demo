package com.tutorial.bootdemo.controller;


import com.tutorial.bootdemo.Response;
import com.tutorial.bootdemo.dto.StudentDTO;
import com.tutorial.bootdemo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @GetMapping("/student/{id}")
    public Response<StudentDTO> getStudentById(@PathVariable long id) {
        log.info("Received request to get student by id. id={}", id);
        StudentDTO studentDTO = studentService.getStudentById(id);
        log.info("Completed request to get student by id. id={}, found={}", id, studentDTO != null);
        log.debug("Student detail returned for id={}: {}", id, studentDTO);
        return Response.newSuccess(studentDTO);
    }

    @GetMapping("/student")
    public Response<List<StudentDTO>> getStudentByAge(@RequestParam int maxAge, @RequestParam int minAge) {
        log.info("Received request to query students by age range. minAge={}, maxAge={}", minAge, maxAge);
        List<StudentDTO> students = studentService.getStudentsByAges(maxAge, minAge);
        log.info("Completed request to query students by age range. minAge={}, maxAge={}, resultCount={}",
                minAge, maxAge, students.size());
        log.debug("Student details returned for age range. minAge={}, maxAge={}, students={}", minAge, maxAge, students);
        return Response.newSuccess(students);
    }


    @PostMapping("/student")
    public Response<Long> addNewStudent(@RequestBody StudentDTO studentDTO) {
        log.info("Received request to add student. name={}, email={}, gender={}",
                studentDTO.getName(), maskEmail(studentDTO.getEmail()), studentDTO.getGender());
        log.debug("Student payload received for creation: {}", studentDTO);
        Long studentId = studentService.addNewStudent(studentDTO);
        log.info("Completed request to add student. id={}, email={}", studentId, maskEmail(studentDTO.getEmail()));
        return Response.newSuccess(studentId);
    }

    @DeleteMapping("/student/{id}")
    public void deleteStudentById(@PathVariable long id) {
        log.info("Received request to delete student by id. id={}", id);
        studentService.deleteStudentById(id);
        log.info("Completed request to delete student by id. id={}", id);
    }

    @PutMapping("/student/{id}")
    public Response<StudentDTO> updateStudentById(@PathVariable long id, @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String email) {
        log.info("Received request to update student. id={}, nameProvided={}, emailProvided={}",
                id, name != null, email != null);
        log.debug("Student update request detail. id={}, name={}, email={}", id, name, maskEmail(email));
        StudentDTO updatedStudent = studentService.updateStudentById(id, name, email);
        log.info("Completed request to update student. id={}, updatedName={}, updatedEmail={}",
                id, updatedStudent.getName(), maskEmail(updatedStudent.getEmail()));
        log.debug("Updated student detail. id={}, student={}", id, updatedStudent);
        return Response.newSuccess(updatedStudent);
    }

    private String maskEmail(String email) {
        if (email == null || email.isBlank()) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***" + (atIndex >= 0 ? email.substring(atIndex) : "");
        }
        return email.charAt(0) + "***" + email.substring(atIndex);
    }
}
