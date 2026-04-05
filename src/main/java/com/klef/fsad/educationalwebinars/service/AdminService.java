package com.klef.fsad.educationalwebinars.service;

import java.util.List;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.StudentResources;

public interface AdminService 
{
    // LOGIN
    public Admin verifyAdminLogin(String username, String password);
    
    // DASHBOARD
    public long getTotalWebinars();
    public long getTotalRegistrations();
    public long getResources();
    public long getLiveSessions();
    
    public double getAttendanceRate();
    public double getAverageRegistrationsPerEvent();
    
    // EVENTS
    public String addEvent(ScheduleEvent scheduleEvent);
    
    // RESOURCES
    public String addResource(StudentResources resource);
    public List<StudentResources> viewAllResources();
    public boolean deleteResource(int id);

    // ANALYTICS
    public long countEventsByCategory(String category);
    public List<Object[]> getMostPopularEvents();
}