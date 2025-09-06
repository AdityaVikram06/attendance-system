package com.college.attendance.attendance.system.controller;

import com.college.attendance.attendance.system.dto.LoginRequest;
import com.college.attendance.attendance.system.dto.RegisterRequest;
import com.college.attendance.attendance.system.model.User;
import com.college.attendance.attendance.system.repository.UserRepository;
import com.college.attendance.attendance.system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register new user and return token + user
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        User user = new User();

        // Use email for admin/faculty, rollNumber for student
        if ("student".equalsIgnoreCase(request.getUserType())) {
            user.setUsername(request.getRollNumber());
            user.setRoles(Set.of("STUDENT"));
        } else if ("faculty".equalsIgnoreCase(request.getUserType())) {
            user.setUsername(request.getEmail());
            user.setRoles(Set.of("FACULTY"));
        } else {
            user.setUsername(request.getEmail());
            user.setRoles(Set.of("ADMIN"));
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save in DB
        userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        // Response format same as login
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return response;
    }

    // ✅ Login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        String username;

        if ("student".equalsIgnoreCase(request.getUserType())) {
            username = request.getRollNumber();
        } else {
            username = request.getEmail();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(username);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return response;
    }

    // ✅ Logout
    @PostMapping("/logout")
    public Map<String, String> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully. Clear token on frontend.");
        return response;
    }
}
