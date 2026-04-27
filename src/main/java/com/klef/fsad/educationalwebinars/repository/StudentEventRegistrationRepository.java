package com.klef.fsad.educationalwebinars.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.fsad.educationalwebinars.entity.StudentEventRegistration;

@Repository
public interface StudentEventRegistrationRepository extends JpaRepository<StudentEventRegistration, Long> {
    List<StudentEventRegistration> findByStudentUsernameOrderByRegisteredAtDesc(String studentUsername);
    boolean existsByStudentUsernameAndEventId(String studentUsername, int eventId);
    long countByEventId(int eventId);
}
