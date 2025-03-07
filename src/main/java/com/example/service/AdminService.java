package com.example.service;

import com.example.entity.Admin;
import com.example.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public boolean emailExists(String email) {
        return adminRepository.existsByEmail(email);
    }
}
