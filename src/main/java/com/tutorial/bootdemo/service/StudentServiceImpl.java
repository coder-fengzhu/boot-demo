package com.tutorial.bootdemo.service;

import com.tutorial.bootdemo.converter.StudentConverter;
import com.tutorial.bootdemo.dao.Student;
import com.tutorial.bootdemo.dao.StudentRepository;
import com.tutorial.bootdemo.dto.StudentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO getStudentById(long id) {
        log.info("Getting student by id: {}", id);
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            log.warn("Student not found by id: {}", id);
            return new RuntimeException("Student not found with id: " + id);
        });
        StudentDTO dto = StudentConverter.convertStudent(student);
        log.info("Found student: {}", dto);
        return dto;
    }

    @Override
    public Long addNewStudent(StudentDTO studentDTO) {
        log.info("Adding new student: {}", studentDTO);
        List<Student> studentList = studentRepository.findByEmail(studentDTO.getEmail());
        if (!CollectionUtils.isEmpty(studentList)) {
            log.warn("Email {} has been taken, rejecting registration", studentDTO.getEmail());
            throw new IllegalStateException("email:" + studentDTO.getEmail() + " has been taken");
        }
        Student student = studentRepository.save(StudentConverter.convertStudent(studentDTO));
        log.info("Successfully added new student with id: {}", student.getId());
        return student.getId();
    }

    @Override
    public void deleteStudentById(long id) {
        log.info("Deleting student by id: {}", id);
        studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to delete non-existent student with id: {}", id);
                    return new IllegalArgumentException("id:" + id + "doesn't exist!");
                });
        studentRepository.deleteById(id);
        log.info("Successfully deleted student with id: {}", id);
    }

    @Override
    @Transactional
    public StudentDTO updateStudentById(long id, String name, String email) {
        log.info("Updating student id={}, name='{}', email='{}'", id, name, email);
        Student studentInDB = studentRepository.findById(id).orElseThrow(() -> {
            log.warn("Attempted to update non-existent student with id: {}", id);
            return new IllegalArgumentException("id:" + id + "doesn't exist!");
        });

        if (StringUtils.hasLength(name) && !studentInDB.getName().equals(name)) {
            log.info("Updating student {} name from '{}' to '{}'", id, studentInDB.getName(), name);
            studentInDB.setName(name);
        }
        if (StringUtils.hasLength(email) && !studentInDB.getEmail().equals(email)) {
            log.info("Updating student {} email from '{}' to '{}'", id, studentInDB.getEmail(), email);
            studentInDB.setEmail(email);
        }
        Student student = studentRepository.save(studentInDB);
        StudentDTO dto = StudentConverter.convertStudent(student);
        log.info("Successfully updated student: {}", dto);
        return dto;
    }

    @Override
    public List<StudentDTO> getStudentsByAges(int maxAge, int minAge) {
        log.info("Querying students by age range: minAge={}, maxAge={}", minAge, maxAge);
        List<Student> studentList = studentRepository.findByAgeBetween(minAge, maxAge);
        if (CollectionUtils.isEmpty(studentList)) {
            log.info("No students found in age range [{}, {}]", minAge, maxAge);
            return List.of();
        }

        List<StudentDTO> result = studentList.stream().map(StudentConverter::convertStudent).collect(Collectors.toList());
        log.info("Found {} students in age range [{}, {}]", result.size(), minAge, maxAge);
        return result;
    }



}
