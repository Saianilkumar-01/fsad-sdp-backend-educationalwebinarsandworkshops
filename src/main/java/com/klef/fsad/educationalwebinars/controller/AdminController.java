package com.klef.fsad.educationalwebinars.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klef.fsad.educationalwebinars.entity.Admin;
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
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password)
    {
        Admin admin = adminService.verifyAdminLogin(username, password);
        
        if(admin != null)
            return ResponseEntity.ok(admin);
        else
            return ResponseEntity.status(401).body("Invalid Credentials");
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