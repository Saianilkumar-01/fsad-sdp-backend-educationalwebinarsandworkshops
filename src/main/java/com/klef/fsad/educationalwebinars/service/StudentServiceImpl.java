
package com.klef.fsad.educationalwebinars.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.klef.fsad.educationalwebinars.entity.ManageEvents;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.Student;
import com.klef.fsad.educationalwebinars.entity.StudentEventRegistration;
import com.klef.fsad.educationalwebinars.repository.ManageEventsRepository;
import com.klef.fsad.educationalwebinars.repository.ScheduleEventRepository;
import com.klef.fsad.educationalwebinars.repository.StudentEventRegistrationRepository;
import com.klef.fsad.educationalwebinars.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService{
	private static final int OTP_LENGTH = 6;
	private static final int OTP_VALIDITY_MINUTES = 5;

	private final SecureRandom random = new SecureRandom();
	private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
	
	@Autowired
	private StudentRepository studentrepo;

	@Autowired
	private ScheduleEventRepository scheduleEventRepository;

	@Autowired
	private ManageEventsRepository manageEventsRepository;

	@Autowired
	private StudentEventRegistrationRepository studentEventRegistrationRepository;

	@Autowired(required = false)
	private JavaMailSender mailSender;

	@Override
	public Student verifystudentlogin(String username, String password) {
		Student s = studentrepo.findByUsernameAndPassword(username, password);
		if(s == null) {
			s = studentrepo.findByEmailAndPassword(username, password);
		}
		return s;
	}

	@Override
	 public String studentRegistration(Student student) {
		    if (studentrepo.existsByUsername(student.getUsername())
		        || studentrepo.existsByEmail(student.getEmail())
		        || studentrepo.existsByContact(student.getContact())) {
		      throw new IllegalArgumentException("DUPLICATE_STUDENT");
		    }
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

	@Override
	public String sendForgotPasswordOtp(String email) {
		if(email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("INVALID_EMAIL");
		}

		String normalizedEmail = email.trim().toLowerCase();
		Student student = studentrepo.findByEmailIgnoreCase(normalizedEmail);
		if(student == null) {
			throw new IllegalArgumentException("EMAIL_NOT_FOUND");
		}

		String otp = generateOtp();
		long expiresAt = System.currentTimeMillis() + (OTP_VALIDITY_MINUTES * 60L * 1000L);
		otpStore.put(normalizedEmail, new OtpData(otp, expiresAt));

		try {
			sendOtpEmail(normalizedEmail, otp);
		} catch (RuntimeException ex) {
			otpStore.remove(normalizedEmail);
			throw ex;
		}

		return "OTP sent to registered email";
	}

	@Override
	public String resetPasswordWithOtp(String email, String otp, String newPassword) {
		if(email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("INVALID_EMAIL");
		}
		if(otp == null || otp.trim().isEmpty()) {
			throw new IllegalArgumentException("INVALID_OTP");
		}
		if(newPassword == null || newPassword.length() < 8) {
			throw new IllegalArgumentException("WEAK_PASSWORD");
		}

		String normalizedEmail = email.trim().toLowerCase();
		Student student = studentrepo.findByEmailIgnoreCase(normalizedEmail);
		if(student == null) {
			throw new IllegalArgumentException("EMAIL_NOT_FOUND");
		}

		OtpData otpData = otpStore.get(normalizedEmail);
		if(otpData == null) {
			throw new IllegalArgumentException("OTP_NOT_REQUESTED");
		}

		if(System.currentTimeMillis() > otpData.getExpiresAt()) {
			otpStore.remove(normalizedEmail);
			throw new IllegalArgumentException("OTP_EXPIRED");
		}

		if(!otpData.getOtp().equals(otp.trim())) {
			throw new IllegalArgumentException("INVALID_OTP");
		}

		student.setPassword(newPassword);
		studentrepo.save(student);
		otpStore.remove(normalizedEmail);

		return "Password updated successfully";
	}

	@Override
	public List<Map<String, Object>> getBrowseEvents() {
		List<ScheduleEvent> events = scheduleEventRepository.findAll();
		List<Map<String, Object>> result = new ArrayList<>();

		for (ScheduleEvent event : events) {
			if (!isEventVisibleToStudent(event.getId())) {
				continue;
			}

			Map<String, Object> map = new LinkedHashMap<>();
			map.put("id", String.valueOf(event.getId()));
			map.put("eventId", event.getId());
			map.put("title", event.getTitle());
			map.put("eventType", event.getEventType());
			map.put("format", normalizeEventType(event.getEventType()));
			map.put("category", event.getCategory());
			map.put("instructorName", event.getInstructorName());
			map.put("speaker", event.getInstructorName());
			map.put("date", event.getDate());
			map.put("time", event.getTime());
			map.put("duration", event.getDuration());
			map.put("capacity", event.getCapacity());
			map.put("registrations", studentEventRegistrationRepository.countByEventId(event.getId()));
			map.put("streamUrl", event.getStreamUrl());
			result.add(map);
		}

		return result;
	}

	@Override
	public List<Map<String, Object>> getMyWebinars(String usernameOrEmail) {
		Student student = resolveStudent(usernameOrEmail);
		if (student == null) {
			throw new IllegalArgumentException("STUDENT_NOT_FOUND");
		}

		List<StudentEventRegistration> registrations =
			studentEventRegistrationRepository.findByStudentUsernameOrderByRegisteredAtDesc(student.getUsername());

		List<Map<String, Object>> result = new ArrayList<>();
		for (StudentEventRegistration registration : registrations) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("id", registration.getId());
			map.put("eventId", String.valueOf(registration.getEventId()));
			map.put("title", registration.getEventTitle());
			map.put("eventType", registration.getEventType());
			map.put("format", normalizeEventType(registration.getEventType()));
			map.put("speaker", registration.getSpeaker());
			map.put("date", registration.getEventDate());
			map.put("status", registration.getStatus());
			result.add(map);
		}

		return result;
	}

	@Override
	public String registerForEvent(String usernameOrEmail, int eventId) {
		Student student = resolveStudent(usernameOrEmail);
		if (student == null) {
			throw new IllegalArgumentException("STUDENT_NOT_FOUND");
		}

		Optional<ScheduleEvent> eventOptional = scheduleEventRepository.findById(eventId);
		if (eventOptional.isEmpty()) {
			throw new IllegalArgumentException("EVENT_NOT_FOUND");
		}

		if (studentEventRegistrationRepository.existsByStudentUsernameAndEventId(student.getUsername(), eventId)) {
			throw new IllegalArgumentException("ALREADY_REGISTERED");
		}

		ScheduleEvent event = eventOptional.get();
		if (!isEventVisibleToStudent(event.getId())) {
			throw new IllegalArgumentException("EVENT_NOT_AVAILABLE");
		}

		long registrationCount = studentEventRegistrationRepository.countByEventId(eventId);
		if (event.getCapacity() > 0 && registrationCount >= event.getCapacity()) {
			throw new IllegalArgumentException("EVENT_FULL");
		}

		StudentEventRegistration registration = new StudentEventRegistration();
		registration.setEventId(event.getId());
		registration.setEventTitle(event.getTitle());
		registration.setEventType(event.getEventType());
		registration.setSpeaker(event.getInstructorName());
		registration.setEventDate(formatEventDate(event.getDate(), event.getTime()));
		registration.setStudentUsername(student.getUsername());
		registration.setStudentEmail(student.getEmail());
		registration.setStudentName(student.getName());
		registration.setStatus("registered");
		studentEventRegistrationRepository.save(registration);

		return "Registered successfully";
	}

	private Student resolveStudent(String usernameOrEmail) {
		if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
			return null;
		}

		String value = usernameOrEmail.trim();
		Optional<Student> byUsername = studentrepo.findById(value);
		if (byUsername.isPresent()) {
			return byUsername.get();
		}

		return studentrepo.findByEmailIgnoreCase(value.toLowerCase());
	}

	private boolean isEventVisibleToStudent(int eventId) {
		List<ManageEvents> manageEvents = manageEventsRepository.findByEventId(eventId);
		if (manageEvents.isEmpty()) {
			return true;
		}

		for (ManageEvents manage : manageEvents) {
			String approval = safeUpper(manage.getApprovalStatus());
			String status = safeUpper(manage.getStatus());
			boolean approved = approval.isEmpty() || "APPROVED".equals(approval);
			boolean availableStatus = !"CANCELLED".equals(status) && !"REJECTED".equals(status);
			if (approved && availableStatus) {
				return true;
			}
		}

		return false;
	}

	private String formatEventDate(String date, String time) {
		String safeDate = date == null ? "Date TBD" : date;
		if (time == null || time.trim().isEmpty()) {
			return safeDate;
		}
		return safeDate + " at " + time;
	}

	private String normalizeEventType(String eventType) {
		if (eventType == null || eventType.trim().isEmpty()) {
			return "Webinar";
		}
		String type = eventType.trim().toLowerCase();
		if ("workshop".equals(type)) {
			return "Workshop";
		}
		return "Webinar";
	}

	private String safeUpper(String value) {
		return value == null ? "" : value.trim().toUpperCase();
	}

	private String generateOtp() {
		int min = (int) Math.pow(10, OTP_LENGTH - 1);
		int otp = random.nextInt(9 * min) + min;
		return String.valueOf(otp);
	}

	private void sendOtpEmail(String toEmail, String otp) {
		if(mailSender == null) {
			throw new IllegalStateException("MAIL_SERVICE_UNAVAILABLE");
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("EduWebinar Password Reset OTP");
			message.setText(
				"Your OTP for password reset is: " + otp + "\n"
				+ "This OTP is valid for " + OTP_VALIDITY_MINUTES + " minutes.\n"
				+ "If you did not request this, please ignore this email."
			);
			mailSender.send(message);
		} catch (Exception e) {
			throw new IllegalStateException("EMAIL_SEND_FAILED");
		}
	}

	private static class OtpData {
		private final String otp;
		private final long expiresAt;

		private OtpData(String otp, long expiresAt) {
			this.otp = otp;
			this.expiresAt = expiresAt;
		}

		private String getOtp() {
			return otp;
		}

		private long getExpiresAt() {
			return expiresAt;
		}
	}

}
