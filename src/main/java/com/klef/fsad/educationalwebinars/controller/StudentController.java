package com.klef.fsad.educationalwebinars.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klef.fsad.educationalwebinars.entity.Student;
import com.klef.fsad.educationalwebinars.security.JwtUtil;
import com.klef.fsad.educationalwebinars.service.CustomUserDetailsService;
import com.klef.fsad.educationalwebinars.service.StudentService;



@RestController
@RequestMapping("studentapi")
@CrossOrigin("*")
public class StudentController {
	
	@Autowired
	private StudentService studentservice;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@GetMapping("/")
	public String studenthome()
	{
		return "student home";
	}
	
//	@PostMapping("/registration")
//	public ResponseEntity<String> studentregistration(@RequestBody Student s)
//	{
//		try
//		{
//			String output=studentservice.studentRegistration(s);
//			return ResponseEntity.status(201).body(output);
//		}
//		catch(Exception e)
//		{
//			 return ResponseEntity.status(500).body("Internal Server Error");
//		}
//	}
	  @PostMapping("/registration")
	  public ResponseEntity<String> studentregistration(@RequestBody Student s) {
	    try {
	      String output = studentservice.studentRegistration(s);
	      return ResponseEntity.status(201).body(output);
	    } catch (IllegalArgumentException ex) {
	      if ("DUPLICATE_STUDENT".equals(ex.getMessage())) {
	        return ResponseEntity.status(409).body("Account already exists");
	      }
	      return ResponseEntity.status(400).body("Bad Request");
	    } catch (Exception e) {
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
				UserDetails userDetails = userDetailsService.loadUserByUsername(s.getUsername());
				String token = jwtUtil.generateToken(userDetails);

				Map<String, Object> response = new HashMap<>();
				response.put("message", "login success");
				response.put("token", token);
				response.put("username", s.getUsername());
				response.put("name", s.getName());
				response.put("email", s.getEmail());
				response.put("role", "STUDENT");

				return ResponseEntity.status(200).body(response);
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

	@PostMapping("/forgotpassword/sendotp")
	public ResponseEntity<String> sendForgotPasswordOtp(@RequestBody Map<String, String> request) {
		try {
			String output = studentservice.sendForgotPasswordOtp(request.get("email"));
			return ResponseEntity.ok(output);
		} catch (IllegalArgumentException ex) {
			if ("INVALID_EMAIL".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("Email is required");
			}
			if ("EMAIL_NOT_FOUND".equals(ex.getMessage())) {
				return ResponseEntity.status(404).body("Student with this email not found");
			}
			return ResponseEntity.status(400).body("Unable to process forgot password request");
		} catch (IllegalStateException ex) {
			if ("MAIL_SERVICE_UNAVAILABLE".equals(ex.getMessage())) {
				return ResponseEntity.status(503).body("Mail service is not configured");
			}
			if ("EMAIL_SEND_FAILED".equals(ex.getMessage())) {
				return ResponseEntity.status(500).body("Failed to send OTP email");
			}
			return ResponseEntity.status(500).body("Unable to send OTP");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@PostMapping("/forgotpassword/reset")
	public ResponseEntity<String> resetForgotPassword(@RequestBody Map<String, String> request) {
		try {
			String output = studentservice.resetPasswordWithOtp(
				request.get("email"),
				request.get("otp"),
				request.get("newPassword")
			);
			return ResponseEntity.ok(output);
		} catch (IllegalArgumentException ex) {
			if ("INVALID_EMAIL".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("Email is required");
			}
			if ("INVALID_OTP".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("Invalid OTP");
			}
			if ("WEAK_PASSWORD".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("Password must be at least 8 characters long");
			}
			if ("EMAIL_NOT_FOUND".equals(ex.getMessage())) {
				return ResponseEntity.status(404).body("Student with this email not found");
			}
			if ("OTP_NOT_REQUESTED".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("Please request OTP first");
			}
			if ("OTP_EXPIRED".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("OTP expired. Please request a new OTP");
			}
			return ResponseEntity.status(400).body("Unable to reset password");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/browseevents")
	public ResponseEntity<?> browseEvents() {
		try {
			return ResponseEntity.ok(studentservice.getBrowseEvents());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@GetMapping("/mywebinars")
	public ResponseEntity<?> myWebinars(@RequestParam("student") String usernameOrEmail) {
		try {
			return ResponseEntity.ok(studentservice.getMyWebinars(usernameOrEmail));
		} catch (IllegalArgumentException ex) {
			if ("STUDENT_NOT_FOUND".equals(ex.getMessage())) {
				return ResponseEntity.status(404).body("Student not found");
			}
			return ResponseEntity.status(400).body("Invalid request");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}

	@PostMapping("/registerevent")
	public ResponseEntity<String> registerEvent(@RequestBody Map<String, Object> request) {
		try {
			Object studentObj = request.get("student");
			Object eventObj = request.get("eventId");

			String student = studentObj == null ? null : studentObj.toString();
			if (student == null || student.trim().isEmpty()) {
				return ResponseEntity.status(400).body("Student identifier is required");
			}

			if (eventObj == null) {
				return ResponseEntity.status(400).body("eventId is required");
			}

			int eventId;
			try {
				eventId = Integer.parseInt(eventObj.toString());
			} catch (NumberFormatException ex) {
				return ResponseEntity.status(400).body("eventId must be a valid number");
			}

			String output = studentservice.registerForEvent(student, eventId);
			return ResponseEntity.status(201).body(output);
		} catch (IllegalArgumentException ex) {
			if ("STUDENT_NOT_FOUND".equals(ex.getMessage())) {
				return ResponseEntity.status(404).body("Student not found");
			}
			if ("EVENT_NOT_FOUND".equals(ex.getMessage())) {
				return ResponseEntity.status(404).body("Event not found");
			}
			if ("ALREADY_REGISTERED".equals(ex.getMessage())) {
				return ResponseEntity.status(409).body("Already registered for this event");
			}
			if ("EVENT_NOT_AVAILABLE".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("Event is not available for registration");
			}
			if ("EVENT_FULL".equals(ex.getMessage())) {
				return ResponseEntity.status(400).body("Event capacity is full");
			}
			return ResponseEntity.status(400).body("Unable to register for event");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Internal Server Error");
		}
	}
	
	
	

}
