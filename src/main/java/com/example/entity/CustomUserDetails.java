package com.example.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private Resident resident;

    public CustomUserDetails(Resident resident) {
        this.resident = resident;
    }

    public Resident getResident() {
        return resident;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Add roles/authorities if needed
    }

    @Override
    public String getPassword() {
        return resident.getPassword();
    }

    @Override
    public String getUsername() {
        return resident.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
