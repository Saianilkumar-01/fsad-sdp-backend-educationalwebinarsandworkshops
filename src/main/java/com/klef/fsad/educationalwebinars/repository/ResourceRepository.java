package com.klef.fsad.educationalwebinars.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.klef.fsad.educationalwebinars.entity.StudentResources;

public interface ResourceRepository extends JpaRepository<StudentResources, Integer>
{
    List<StudentResources> findByEvent(String event);
}