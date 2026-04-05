package com.klef.fsad.educationalwebinars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klef.fsad.educationalwebinars.entity.Student;

public interface StudentRepository extends JpaRepository<Student, String> {

}
