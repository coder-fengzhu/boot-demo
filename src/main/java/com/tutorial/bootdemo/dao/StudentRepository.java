package com.tutorial.bootdemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    // find支持的关键字， And , Between, In, StaringWith
    List<Student> findByEmail(String email);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findByNameStartingWith(String namePrefix);

    // 通过原生sql/jqpl查询
    @Query(value = "select new com.tutorial.bootdemo.dao.Student(name, email) from Student  where email = :email")
    List<Student> findByEmail2(@Param("email") String email);

}
