package com.klef.fsad.educationalwebinars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klef.fsad.educationalwebinars.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String>
{
    Admin findByUsername(String username);   // ✅ CHANGED
}