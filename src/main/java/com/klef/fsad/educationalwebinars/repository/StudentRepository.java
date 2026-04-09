package com.klef.fsad.educationalwebinars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.fsad.educationalwebinars.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String>
{
    public Student findByUsernameAndPassword(String username, String password);
    public Student findByEmailAndPassword(String email, String password);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByContact(String contact);
}