package com.klef.fsad.educationalwebinars.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.StudentResources;
import com.klef.fsad.educationalwebinars.repository.AdminRepository;
import com.klef.fsad.educationalwebinars.repository.StudentRepository;

public class AdminServiceImpl implements AdminService
{
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public Admin verifyAdminLogin(String username, String password) 
	{
		return adminRepository.findByUsernameAndPassword(username,password);
	}

	@Override
	public long getTotalWebinars() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalRegistrations() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getResources() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLiveSessions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAttendanceRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAverageRegistrationsPerEvent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String AddEvent(ScheduleEvent scheduleEvent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addResource(StudentResources resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StudentResources> viewAllResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteResource(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long countEventsByCategory(String category) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object[]> getMostPopularEvents() {
		// TODO Auto-generated method stub
		return null;
	}

}
