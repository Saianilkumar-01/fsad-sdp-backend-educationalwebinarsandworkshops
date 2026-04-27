package com.klef.fsad.educationalwebinars.service;

import java.util.List;
import java.util.Map;

import com.klef.fsad.educationalwebinars.entity.Student;

public interface StudentService 
{
	public Student verifystudentlogin(String username, String password);
	public String studentRegistration(Student student);
	public String updateprofile(Student student);
	public Boolean deleteStudentaccount(String username);
	public String sendForgotPasswordOtp(String email);
	public String resetPasswordWithOtp(String email, String otp, String newPassword);
	public List<Map<String, Object>> getBrowseEvents();
	public List<Map<String, Object>> getMyWebinars(String usernameOrEmail);
	public String registerForEvent(String usernameOrEmail, int eventId);
	

}
