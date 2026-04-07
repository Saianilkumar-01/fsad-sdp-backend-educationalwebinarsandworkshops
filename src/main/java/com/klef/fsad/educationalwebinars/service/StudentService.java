package com.klef.fsad.educationalwebinars.service;

import com.klef.fsad.educationalwebinars.entity.Student;

public interface StudentService 
{
	public Student verifystudentlogin(String username, String password);
	public String studentRegistration(Student student);
	public String updateprofile(Student student);
	

}
