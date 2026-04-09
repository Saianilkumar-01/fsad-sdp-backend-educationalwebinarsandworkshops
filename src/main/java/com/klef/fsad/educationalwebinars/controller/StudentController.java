package com.klef.fsad.educationalwebinars.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.fsad.educationalwebinars.entity.Student;
import com.klef.fsad.educationalwebinars.service.StudentService;



@RestController
@RequestMapping("studentapi")
@CrossOrigin("*")
public class StudentController {
	
	@Autowired
	private StudentService studentservice;
	
	@GetMapping("/")
	public String studenthome()
	{
		return "student home";
	}
	
	@PostMapping("/registration")
	public ResponseEntity<String> studentregistration(@RequestBody Student s)
	{
		try
		{
			String output=studentservice.studentRegistration(s);
			return ResponseEntity.status(201).body(output);
		}
		catch(Exception e)
		{
			 return ResponseEntity.status(500).body("Internal Server Error");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> verifystudentlogin(@RequestBody Student student)
	{
		try
		{
			Student s=studentservice.verifystudentlogin(student.getUsername(), student.getPassword());
			if(s!=null)
			{
				return ResponseEntity.status(200).body(s);
		    }
			else
			{
				return ResponseEntity.status(401).body("Login Invalid");
			}
		}
		catch(Exception e)
		{
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}
	
	@PostMapping("/updateprofile")
	public ResponseEntity<String> updateprofile(@RequestBody Student s)
	{
		try
		{
			String output=studentservice.updateprofile(s);
			return ResponseEntity.status(201).body(output);
		}
		catch(Exception e)
		{
			return ResponseEntity.status(500).body("Internal Server Error");
		}
		
	}
	
	@DeleteMapping("/deletestudent/{username}")
	public ResponseEntity<String> deleteServiceManager(@PathVariable String username)
	{
	    try
	    {
	        boolean deleted = studentservice.deleteStudentaccount(username);

	        if(deleted)
	        {
	            return ResponseEntity.ok("Student Account Deleted Successfully");
	        }
	        else
	        {
	            return ResponseEntity.status(404).body("Student Account Not Found");
	        }
	    }
	    catch(Exception e)
	    {
	        //return ResponseEntity.status(500).body("Internal Server Error");
	    	return ResponseEntity.status(500).body(e.getMessage());
	    }
	}
	
	
	

}
