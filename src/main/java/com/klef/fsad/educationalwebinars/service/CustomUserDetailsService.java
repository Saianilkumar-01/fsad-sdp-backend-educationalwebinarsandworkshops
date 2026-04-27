package com.klef.fsad.educationalwebinars.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.klef.fsad.educationalwebinars.entity.Admin;
import com.klef.fsad.educationalwebinars.entity.Student;
import com.klef.fsad.educationalwebinars.repository.AdminRepository;
import com.klef.fsad.educationalwebinars.repository.StudentRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService 
{
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        // First check if it's an Admin
        Admin admin = adminRepository.findById(username).orElse(null);
        if (admin != null) 
        {
            return new User(
                    admin.getUsername(),
                    admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        // Then check if it's a Student
        Student student = studentRepository.findById(username).orElse(null);
        if (student != null) 
        {
            return new User(
                    student.getUsername(),
                    student.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"))
            );
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
