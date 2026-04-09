package com.klef.fsad.educationalwebinars.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.ManageEvents;
import com.klef.fsad.educationalwebinars.entity.ScheduleEvent;
import com.klef.fsad.educationalwebinars.entity.StudentResources;
import com.klef.fsad.educationalwebinars.service.AdminService;

@RestController
@RequestMapping("/adminapi")
@CrossOrigin("*")
public class AdminController 
{
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/")
    public String home()
    {
        return "Admin Controller is Working";
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin adminReq)
    {
        try
        {
            Admin admin = adminService.verifyAdminLogin(adminReq.getUsername(), adminReq.getPassword());

            if(admin != null)
                return ResponseEntity.status(200).body("login success");
            else
                return ResponseEntity.status(401).body("invalid credentials");
        }
        catch(Exception e)
        {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    // Create admin credentials (use Postman to add specific admin). This acts like a registration
    // but keep it restricted in production. For now it simply inserts the admin into the DB.
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

    // ================= DASHBOARD =================
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

    // ================= EVENTS =================
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
                    map.put("id", me.getId()); // ManageEvents primary key
                    map.put("status", me.getStatus());
                    map.put("approvalStatus", me.getApprovalStatus());
                    map.put("remarks", me.getRemarks());
                    foundManageEntity = true;
                    break;
                }
            }
            
            // If the event was created without a manage event record, give it defaults
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

    // ================= RESOURCES =================
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

    // ================= ANALYTICS =================
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