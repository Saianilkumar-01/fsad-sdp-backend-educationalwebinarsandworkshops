package com.klef.fsad.educationalwebinars.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.fsad.educationalwebinars.entity.ManageEvents;

@Repository
public interface ManageEventsRepository 
        extends JpaRepository<ManageEvents, Integer>
{
    // Find by status
    List<ManageEvents> findByStatus(String status);

    // Find by eventId
    List<ManageEvents> findByEventId(int eventId);
}