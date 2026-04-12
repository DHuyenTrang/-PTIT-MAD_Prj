package com.traffic.traffic_be.controller;

import com.traffic.traffic_be.dto.Request.ForgotPasswordRequest;
import com.traffic.traffic_be.dto.Request.LoginRequest;
import com.traffic.traffic_be.dto.Request.RegisterRequest;
import com.traffic.traffic_be.dto.Request.ResetPasswordRequest;
import com.traffic.traffic_be.dto.Response.ApiResponse;
import com.traffic.traffic_be.dto.Response.LoginResponse;
import com.traffic.traffic_be.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Các API Đăng ký, Đăng nhập và Khôi phục mật khẩu")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản mới")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        String result = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công!", result));
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập và nhận Token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse result = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công!", result));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Yêu cầu gửi mã OTP khôi phục mật khẩu về Email")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String message = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Đặt lại mật khẩu mới bằng mã OTP")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String message = authService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }
}
