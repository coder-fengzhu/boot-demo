package com.tutorial.bootdemo.service;

import com.tutorial.bootdemo.converter.StudentConverter;
import com.tutorial.bootdemo.dao.Student;
import com.tutorial.bootdemo.dao.StudentRepository;
import com.tutorial.bootdemo.dto.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO getStudentById(long id) {
        log.info("Start getting student by id. id={}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student not found by id. id={}", id);
                    return new RuntimeException("id:" + id + " doesn't exist!");
                });
        log.debug("Found student entity by id. id={}, student={}", id, student);
        StudentDTO studentDTO = StudentConverter.convertStudent(student);
        log.info("Finished getting student by id. id={}, studentName={}, email={}",
                id, studentDTO.getName(), maskEmail(studentDTO.getEmail()));
        log.debug("Converted student dto by id. id={}, studentDTO={}", id, studentDTO);
        return studentDTO;
    }

    @Override
    public Long addNewStudent(StudentDTO studentDTO) {
        log.info("Start adding new student. name={}, email={}, gender={}",
                studentDTO.getName(), maskEmail(studentDTO.getEmail()), studentDTO.getGender());
        log.debug("New student dto before validation: {}", studentDTO);
        List<Student> studentList = studentRepository.findByEmail(studentDTO.getEmail());
        log.debug("Email uniqueness query completed. email={}, matchedCount={}",
                maskEmail(studentDTO.getEmail()), studentList.size());
        if (!CollectionUtils.isEmpty(studentList)) {
            log.warn("Failed to add student because email has been taken. email={}, matchedCount={}",
                    maskEmail(studentDTO.getEmail()), studentList.size());
            throw new IllegalStateException("email:" + studentDTO.getEmail() + " has been taken");
        }
        Student studentToSave = StudentConverter.convertStudent(studentDTO);
        log.debug("Converted student entity before save: {}", studentToSave);
        Student student = studentRepository.save(studentToSave);
        log.info("Finished adding new student. id={}, email={}", student.getId(), maskEmail(student.getEmail()));
        log.debug("Saved student entity: {}", student);
        return student.getId();
    }

    @Override
    public void deleteStudentById(long id) {
        log.info("Start deleting student by id. id={}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to delete student because id does not exist. id={}", id);
                    return new IllegalArgumentException("id:" + id + "doesn't exist!");
                });
        log.debug("Student entity found before delete. id={}, student={}", id, student);
        studentRepository.deleteById(id);
        log.info("Finished deleting student by id. id={}", id);
    }

    @Override
    @Transactional
    public StudentDTO updateStudentById(long id, String name, String email) {
        log.info("Start updating student. id={}, nameProvided={}, emailProvided={}", id, StringUtils.hasLength(name), StringUtils.hasLength(email));
        log.debug("Update student request detail. id={}, name={}, email={}", id, name, maskEmail(email));
        Student studentInDB = studentRepository.findById(id).orElseThrow(() -> {
            log.warn("Failed to update student because id does not exist. id={}", id);
            return new IllegalArgumentException("id:" + id + "doesn't exist!");
        });
        log.debug("Student entity before update. id={}, student={}", id, studentInDB);

        if (StringUtils.hasLength(name) && !Objects.equals(studentInDB.getName(), name)) {
            log.info("Updating student name. id={}, oldName={}, newName={}", id, studentInDB.getName(), name);
            studentInDB.setName(name);
        } else {
            log.debug("Student name is not changed. id={}, nameProvided={}, currentName={}",
                    id, StringUtils.hasLength(name), studentInDB.getName());
        }
        if (StringUtils.hasLength(email) && !Objects.equals(studentInDB.getEmail(), email)) {
            log.info("Updating student email. id={}, oldEmail={}, newEmail={}",
                    id, maskEmail(studentInDB.getEmail()), maskEmail(email));
            studentInDB.setEmail(email);
        } else {
            log.debug("Student email is not changed. id={}, emailProvided={}, currentEmail={}",
                    id, StringUtils.hasLength(email), maskEmail(studentInDB.getEmail()));
        }
        Student student = studentRepository.save(studentInDB);
        log.debug("Student entity after save. id={}, student={}", id, student);
        StudentDTO studentDTO = StudentConverter.convertStudent(student);
        log.info("Finished updating student. id={}, name={}, email={}",
                id, studentDTO.getName(), maskEmail(studentDTO.getEmail()));
        log.debug("Updated student dto. id={}, studentDTO={}", id, studentDTO);
        return studentDTO;
    }

    @Override
    public List<StudentDTO> getStudentsByAges(int maxAge, int minAge) {
        log.info("Start querying students by age range. minAge={}, maxAge={}", minAge, maxAge);
        List<Student> studentList = studentRepository.findByAgeBetween(minAge, maxAge);
        log.debug("Student age range query completed. minAge={}, maxAge={}, matchedCount={}",
                minAge, maxAge, studentList.size());
        if (CollectionUtils.isEmpty(studentList)) {
            log.info("Finished querying students by age range. minAge={}, maxAge={}, resultCount=0", minAge, maxAge);
            return List.of();
        }

        List<StudentDTO> studentDTOList = studentList.stream().map(StudentConverter::convertStudent).collect(Collectors.toList());
        log.info("Finished querying students by age range. minAge={}, maxAge={}, resultCount={}",
                minAge, maxAge, studentDTOList.size());
        log.debug("Student dtos returned for age range. minAge={}, maxAge={}, students={}", minAge, maxAge, studentDTOList);
        return studentDTOList;
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
