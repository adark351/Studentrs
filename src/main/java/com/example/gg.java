package com.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class gg {
    public static void main(String[] args) {
        // Declare passwordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // The raw password you want to check
        String rawPassword = "qwerty123456";  // Replace with the password you're testing

        // The stored hashed password (from your database)
        String storedHash = "$2a$10$4nfKB04oKdQq8V0.VHP6gOTHdWPbDGGNgAC5Z1LwOu.yPTLFqofjy";
        String g = passwordEncoder.encode(rawPassword);
        // Check if the raw password matches the stored hash
        boolean match = passwordEncoder.matches(g, storedHash);

        // Output the result
        System.out.println("Match: " + match);
        System.out.println("Hash: " + g);
        System.out.println(passwordEncoder.encode(g));

    }
}
