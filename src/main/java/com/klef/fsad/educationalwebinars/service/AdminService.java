package com.klef.fsad.educationalwebinars.service;

import java.util.List;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.StudentResources;

public interface AdminService 
{
	public Admin verifyAdminLogin(String username , String password);
	
	public long getTotalWebinars();
	public long getTotalRegistrations();
	public long getResources();
	public long getLiveSessions();
	
	 public double getAttendanceRate();
	 public double getAverageRegistrationsPerEvent();
	
	public String AddEvent(ScheduleEvent scheduleEvent);
	
	public String addResource(StudentResources resource);
    public List<StudentResources> viewAllResources();
    public boolean deleteResource(int id);
    

   

    public long countEventsByCategory(String category);
    public List<Object[]> getMostPopularEvents();
}
