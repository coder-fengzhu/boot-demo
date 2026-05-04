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
        log.debug("Fetching student from database by id: {}", id);
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            log.error("Student not found with id: {}", id);
            return new RuntimeException("Student not found with id: " + id);
        });
        log.debug("Found student: {}", student);
        return StudentConverter.convertStudent(student);
    }

    @Override
    public Long addNewStudent(StudentDTO studentDTO) {
        log.debug("Checking if email already exists: {}", studentDTO.getEmail());
        List<Student> studentList = studentRepository.findByEmail(studentDTO.getEmail());
        if (!CollectionUtils.isEmpty(studentList)) {
            log.warn("Email already taken: {}", studentDTO.getEmail());
            throw new IllegalStateException("email:" + studentDTO.getEmail() + " has been taken");
        }
        Student student = studentRepository.save(StudentConverter.convertStudent(studentDTO));
        log.info("New student created with id: {}", student.getId());
        return student.getId();
    }

    @Override
    public void deleteStudentById(long id) {
        log.debug("Checking if student exists for deletion, id: {}", id);
        studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found for deletion, id: {}", id);
                    return new IllegalArgumentException("id:" + id + "doesn't exist!");
                });
        studentRepository.deleteById(id);
        log.info("Student deleted successfully, id: {}", id);
    }

    @Override
    @Transactional
    public StudentDTO updateStudentById(long id, String name, String email) {
        log.debug("Fetching student for update, id: {}", id);
        Student studentInDB = studentRepository.findById(id).orElseThrow(() -> {
            log.error("Student not found for update, id: {}", id);
            return new IllegalArgumentException("id:" + id + "doesn't exist!");
        });

        if (StringUtils.hasLength(name) && !studentInDB.getName().equals(name)) {
            log.debug("Updating student name from '{}' to '{}'", studentInDB.getName(), name);
            studentInDB.setName(name);
        }
        if (StringUtils.hasLength(email) && !studentInDB.getEmail().equals(email)) {
            log.debug("Updating student email from '{}' to '{}'", studentInDB.getEmail(), email);
            studentInDB.setEmail(email);
        }
        Student student = studentRepository.save(studentInDB);
        log.info("Student updated successfully, id: {}", id);
        return StudentConverter.convertStudent(student);
    }

    @Override
    public List<StudentDTO> getStudentsByAges(int maxAge, int minAge) {
        log.debug("Querying students by age range: {} to {}", minAge, maxAge);
        List<Student> studentList = studentRepository.findByAgeBetween(minAge, maxAge);
        if (CollectionUtils.isEmpty(studentList)) {
            log.debug("No students found in age range: {} to {}", minAge, maxAge);
            return List.of();
        }
        log.debug("Found {} students in age range: {} to {}", studentList.size(), minAge, maxAge);
        return studentList.stream().map(StudentConverter::convertStudent).collect(Collectors.toList());
    }

}
