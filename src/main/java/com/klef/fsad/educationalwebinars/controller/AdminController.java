package com.klef.fsad.educationalwebinars.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.ManageEvents;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.StudentResources;
import com.klef.fsad.educationalwebinars.security.JwtUtil;
import com.klef.fsad.educationalwebinars.service.AdminService;
import com.klef.fsad.educationalwebinars.service.CustomUserDetailsService;

@RestController
@RequestMapping("/adminapi")
@CrossOrigin("*")
public class AdminController 
{
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @GetMapping("/")
    public String home()
    {
        return "Admin Controller is Working";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin adminReq)
    {
        try
        {
            Admin admin = adminService.verifyAdminLogin(adminReq.getUsername(), adminReq.getPassword());

            if(admin != null)
            {
                UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getUsername());
                String token = jwtUtil.generateToken(userDetails);

                Map<String, String> response = new HashMap<>();
                response.put("message", "login success");
                response.put("token", token);
                response.put("username", admin.getUsername());
                response.put("role", "ADMIN");

                return ResponseEntity.status(200).body(response);
            }
            else
                return ResponseEntity.status(401).body("invalid credentials");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@RequestBody Admin admin)
    {
        try
        {
            String output = adminService.addAdmin(admin);
            return ResponseEntity.status(201).body(output);
        }
        catch(Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard()
    {
        return ResponseEntity.ok(new Object() {
            public final long totalWebinars = adminService.getTotalWebinars();
            public final long totalRegistrations = adminService.getTotalRegistrations();
            public final long totalResources = adminService.getResources();
            public final long liveSessions = adminService.getLiveSessions();
            public final double attendanceRate = adminService.getAttendanceRate();
            public final double avgRegistrations = adminService.getAverageRegistrationsPerEvent();
        });
    }

    @PostMapping("/addevent")
    public ResponseEntity<String> addEvent(@RequestBody ScheduleEvent event)
    {
        return ResponseEntity.ok(adminService.addEvent(event));
    }

    @GetMapping("/viewevents")
    public ResponseEntity<List<ScheduleEvent>> viewAllEvents()
    {
        return ResponseEntity.ok(adminService.viewAllEvents());
    }

    @GetMapping("/viewevents/{category}")
    public ResponseEntity<List<ScheduleEvent>> viewEventsByCategory(@PathVariable String category)
    {
        return ResponseEntity.ok(adminService.viewEventsByCategory(category));
    }

    @GetMapping("/manageevents")
    public ResponseEntity<List<java.util.Map<String, Object>>> viewAllManageEvents()
    {
        List<ManageEvents> manageEvents = adminService.viewAllManageEvents();
        List<ScheduleEvent> scheduleEvents = adminService.viewAllEvents();

        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for(ScheduleEvent se : scheduleEvents) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("eventId", se.getId());
            map.put("title", se.getTitle());
            map.put("category", se.getCategory());
            map.put("eventType", se.getEventType());
            map.put("date", se.getDate());
            map.put("time", se.getTime());
            map.put("duration", se.getDuration());
            map.put("instructorName", se.getInstructorName());

            boolean foundManageEntity = false;
            for(ManageEvents me : manageEvents) {
                if(me.getEventId() == se.getId()) {
                    map.put("id", me.getId());
                    map.put("status", me.getStatus());
                    map.put("approvalStatus", me.getApprovalStatus());
                    map.put("remarks", me.getRemarks());
                    foundManageEntity = true;
                    break;
                }
            }
            
            if(!foundManageEntity) {
                map.put("id", -1);
                map.put("status", "UPCOMING");
                map.put("approvalStatus", "PENDING");
                map.put("remarks", "");
            }
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteevent/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable int id)
    {
        boolean deleted = adminService.deleteEvent(id);
        if(deleted) {
            return ResponseEntity.ok("Event deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Event not found");
        }
    }

    @PostMapping("/addresource")
    public ResponseEntity<String> addResource(@RequestBody StudentResources resource)
    {
        return ResponseEntity.ok(adminService.addResource(resource));
    }

    @GetMapping("/viewresources")
    public ResponseEntity<List<StudentResources>> viewResources()
    {
        return ResponseEntity.ok(adminService.viewAllResources());
    }

    @DeleteMapping("/deleteresource/{id}")
    public ResponseEntity<String> deleteResource(@PathVariable int id)
    {
        boolean deleted = adminService.deleteResource(id);
        
        if(deleted)
            return ResponseEntity.ok("Resource Deleted Successfully");
        else
            return ResponseEntity.status(404).body("Resource Not Found");
    }

    @GetMapping("/eventcount")
    public ResponseEntity<Long> countByCategory(@RequestParam String category)
    {
        return ResponseEntity.ok(adminService.countEventsByCategory(category));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> mostPopularEvents()
    {
        return ResponseEntity.ok(adminService.getMostPopularEvents());
    }
}