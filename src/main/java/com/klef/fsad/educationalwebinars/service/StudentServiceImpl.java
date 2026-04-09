package com.klef.fsad.educationalwebinars.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.fsad.educationalwebinars.entity.Student;
import com.klef.fsad.educationalwebinars.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentRepository studentrepo;

	@Override
	public Student verifystudentlogin(String username, String password) {
		return studentrepo.findByUsernameAndPassword(username, password);
	}

	@Override
	public String studentRegistration(Student student) {
		studentrepo.save(student);
		return "Student Registered Successfully";
	}

	@Override
	public String updateprofile(Student student) {
		Optional<Student> optional=studentrepo.findById(student.getUsername());
		
		if(optional.isPresent())
		{
			Student s=optional.get();
			
			s.setContact(student.getContact());
			s.setName(student.getName());
			studentrepo.save(s);
			
			return "student profile updated successfully";
			
		}
		else
		{
			return "student username notfound";
		}
	}

	@Override
	public Boolean deleteStudentaccount(String username) {
		if(studentrepo.existsById(username))
		{
			studentrepo.deleteById(username);
			return true;
		}
		return false;
	}

}
