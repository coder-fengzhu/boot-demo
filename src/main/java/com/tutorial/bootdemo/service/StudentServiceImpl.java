package com.tutorial.bootdemo.service;

import com.tutorial.bootdemo.converter.StudentConverter;
import com.tutorial.bootdemo.dao.Student;
import com.tutorial.bootdemo.dao.StudentRepository;
import com.tutorial.bootdemo.dto.StudentDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    /**
     Autowired 是 Spring 提供的注解，@Resource 是 JDK 提供的注解。
     Autowired 默认的注入方式为byType（根据类型进行匹配），
     Resource 默认注入方式为 byName（根据名称进行匹配）。

     当一个接口只存在一个实现类的情况下，
     Resource若根据Name无法匹配，会转换成使用byType匹配，

     当一个接口存在多个实现类的情况下，
     Autowired 和@Resource都需要通过名称才能正确匹配到对应的 Bean。
     Autowired 可以通过 @Qualifier 注解来显示指定名称，
     Resource可以通过 name 属性来显示指定名称。
     * */
    @Resource
    @Qualifier("demoServiceImpl1")
    private DemoService demoService;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO getStudentById(long id) {
        demoService.someMethod();
        Student student = studentRepository.findById(id).orElseThrow(RuntimeException::new);
        return StudentConverter.convertStudent(student);
    }

    @Override
    public Long addNewStudent(StudentDTO studentDTO) {
        List<Student> studentList = studentRepository.findByEmail(studentDTO.getEmail());
        if (!CollectionUtils.isEmpty(studentList)) {
            throw new IllegalStateException("email:" + studentDTO.getEmail() + " has been taken");
        }
        Student student = studentRepository.save(StudentConverter.convertStudent(studentDTO));
        return student.getId();
    }

    @Override
    public void deleteStudentById(long id) {
        studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id:" + id + "doesn't exist!"));
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public StudentDTO updateStudentById(long id, String name, String email) {
        Student studentInDB = studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id:" + id + "doesn't exist!"));

        if (StringUtils.hasLength(name) && !studentInDB.getName().equals(name)) {
            studentInDB.setName(name);
        }
        if (StringUtils.hasLength(email) && !studentInDB.getEmail().equals(email)) {
            studentInDB.setEmail(email);
        }
        Student student = studentRepository.save(studentInDB);
        return StudentConverter.convertStudent(student);
    }


}
