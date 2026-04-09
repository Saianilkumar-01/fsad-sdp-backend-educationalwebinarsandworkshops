package com.klef.fsad.educationalwebinars.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;

public interface ScheduleEventRepository extends JpaRepository<ScheduleEvent, Integer>
{
    long countByEventType(String eventType);
    long countByCategory(String category);
    List<ScheduleEvent> findByCategory(String category);
}