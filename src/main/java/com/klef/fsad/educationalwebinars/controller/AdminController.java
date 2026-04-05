package com.klef.fsad.educationalwebinars.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ROOT API
    @GetMapping("/")
    public String home() {
        return "SDP 38 Project Running Successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody Admin admin) {
        System.out.println(admin.getUsername());  // debug
        System.out.println(admin.getPassword());  // debug

        Admin a = adminService.verifyAdminLogin(admin.getUsername(), admin.getPassword());

        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.status(401).body("Login Failed");
        }
    }
}
