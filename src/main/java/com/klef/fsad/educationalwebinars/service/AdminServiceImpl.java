package com.klef.fsad.educationalwebinars.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.ManageEvents;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.StudentResources;
import com.klef.fsad.educationalwebinars.repository.AdminRepository;
import com.klef.fsad.educationalwebinars.repository.ManageEventsRepository;
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

    @Autowired
    private ManageEventsRepository manageEventsRepository;

    @Override
    public Admin verifyAdminLogin(String username, String password) 
    {
        return adminRepository.findByUsernameAndPassword(username, password);
    }

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
        return 0;
    }

    @Override
    public double getAttendanceRate() 
    {
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

    @Override
    public String addEvent(ScheduleEvent scheduleEvent) 
    {
        ScheduleEvent saved = eventRepository.save(scheduleEvent);

        ManageEvents manage = new ManageEvents();
        manage.setEventId(saved.getId());
        manage.setStatus("UPCOMING");
        manage.setApprovalStatus("PENDING");
        manage.setRemarks("");
        manageEventsRepository.save(manage);

        return "Event Added Successfully";
    }

    @Override
    public List<ScheduleEvent> viewAllEvents() 
    {
        return eventRepository.findAll();
    }

    @Override
    public List<ScheduleEvent> viewEventsByCategory(String category) 
    {
        return eventRepository.findByCategory(category);
    }

    @Override
    public List<ManageEvents> viewAllManageEvents() 
    {
        return manageEventsRepository.findAll();
    }

    @Override
    public boolean deleteEvent(int eventId) 
    {
        if (eventRepository.existsById(eventId)) 
        {
            List<ManageEvents> manageEventsList = manageEventsRepository.findByEventId(eventId);
            if (!manageEventsList.isEmpty()) {
                manageEventsRepository.deleteAll(manageEventsList);
            }
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }

    @Override
    public String addAdmin(Admin admin) {
        adminRepository.save(admin);
        return "Admin Created Successfully";
    }

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

    @Override
    public long countEventsByCategory(String category) 
    {
        return eventRepository.countByCategory(category);
    }

    @Override
    public List<Object[]> getMostPopularEvents() 
    {
        return null;
    }
}