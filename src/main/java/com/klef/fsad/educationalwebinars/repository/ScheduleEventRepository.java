package com.klef.fsad.educationalwebinars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klef.fsad.educationalwebinars.entity.StudentResources;

public interface ScheduleEventRepository extends JpaRepository<StudentResources, Integer>
{

}
