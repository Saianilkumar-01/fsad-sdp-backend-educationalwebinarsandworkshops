package com.klef.fsad.educationalwebinars.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.StudentResources;
import com.klef.fsad.educationalwebinars.repository.AdminRepository;
import com.klef.fsad.educationalwebinars.repository.ScheduleEventRepository;
import com.klef.fsad.educationalwebinars.repository.ResourceRepository;

@Service
public class AdminServiceImpl implements AdminService
{
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ScheduleEventRepository eventRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    // ================= LOGIN =================
    @Override
    public Admin verifyAdminLogin(String username, String password) 
    {
        return adminRepository.findByUsernameAndPassword(username, password);
    }

    // ================= DASHBOARD =================
    @Override
    public long getTotalWebinars() 
    {
        return eventRepository.count();
    }

    @Override
    public long getResources() 
    {
        return resourceRepository.count();
    }

    @Override
    public long getLiveSessions() 
    {
        return eventRepository.countByEventType("LIVE");
    }

    @Override
    public long getTotalRegistrations() 
    {
        // If you don't have registration table yet
        return 0;
    }

    @Override
    public double getAttendanceRate() 
    {
        // dummy logic (update later when attendance table added)
        return 75.0;
    }

    @Override
    public double getAverageRegistrationsPerEvent() 
    {
        long totalEvents = eventRepository.count();
        long totalRegistrations = getTotalRegistrations();

        if (totalEvents == 0) return 0;

        return (double) totalRegistrations / totalEvents;
    }

    // ================= EVENTS =================
    @Override
    public String addEvent(ScheduleEvent scheduleEvent) 
    {
        eventRepository.save(scheduleEvent);
        return "Event Added Successfully";
    }

    // ================= RESOURCES =================
    @Override
    public String addResource(StudentResources resource) 
    {
        resourceRepository.save(resource);
        return "Resource Uploaded Successfully";
    }

    @Override
    public List<StudentResources> viewAllResources() 
    {
        return resourceRepository.findAll();
    }

    @Override
    public boolean deleteResource(int id) 
    {
        if(resourceRepository.existsById(id))
        {
            resourceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ================= ANALYTICS =================
    @Override
    public long countEventsByCategory(String category) 
    {
        return eventRepository.countByCategory(category);
    }

    @Override
    public List<Object[]> getMostPopularEvents() 
    {
        // Placeholder (needs registration table later)
        return null;
    }
}