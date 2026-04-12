package com.traffic.traffic_be.service;

import com.traffic.traffic_be.dto.Request.LoginRequest;
import com.traffic.traffic_be.dto.Request.RegisterRequest;
import com.traffic.traffic_be.dto.Response.LoginResponse;

public interface AuthService {
    String register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    String forgotPassword(String email);
    String resetPassword(String email, String otp, String newPassword);
}

