package com.example.service;

import com.example.entity.Admin;
import com.example.entity.Resident;
import com.example.repository.AdminRepository;
import com.example.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to authenticate user: " + username);

        // Check Admin table
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin != null) {
            System.out.println("Admin found: " + admin.getUsername());
            System.out.println("Stored Password: " + admin.getPassword()); // Debug password
            return org.springframework.security.core.userdetails.User.withUsername(admin.getUsername())
                    .password(admin.getPassword())  // Password remains encoded
                    .roles("ADMIN")
                    .build();
        }

        // Check Resident table
        Resident resident = residentRepository.findByEmail(username).orElse(null);
        if (resident != null) {
            System.out.println("Resident found: " + resident.getEmail());
            return org.springframework.security.core.userdetails.User.withUsername(resident.getEmail())
                    .password(resident.getPassword())  // Password remains encoded
                    .roles("RESIDENT")
                    .build();
        }

        System.out.println("User not found: " + username);
        throw new UsernameNotFoundException("User not found");
    }

}
