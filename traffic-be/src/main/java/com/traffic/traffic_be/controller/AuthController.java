package com.traffic.traffic_be.controller;

import com.traffic.traffic_be.dto.Request.*;
import com.traffic.traffic_be.dto.Response.*;
import com.traffic.traffic_be.service.AuthService;
import com.traffic.traffic_be.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v5/auth")
@RequiredArgsConstructor
@Tag(name = "Auth v5", description = "API Xác thực người dùng")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @Operation(summary = "Gửi OTP đăng nhập về email")
    public ResponseEntity<ApiResponse<Void>> sendLoginOtp(@Valid @RequestBody EmailRequest request) {
        authService.sendLoginOtp(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("OTP đã được gửi đến email của bạn!", null));
    }

    @PostMapping("/login-with-password")
    @Operation(summary = "Đăng nhập bằng email + mật khẩu")
    public ResponseEntity<ApiResponse<AuthDataResponse>> loginWithPassword(
            @Valid @RequestBody LoginWithPasswordRequest request) {
        AuthDataResponse data = authService.loginWithPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công!", data));
    }

    @PostMapping("/verify-otp-code")
    @Operation(summary = "Xác thực OTP (đăng nhập OTP / đăng ký / quên mật khẩu)")
    public ResponseEntity<ApiResponse<AuthDataResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        AuthDataResponse data = authService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.success("Xác thực thành công!", data));
    }

    @PostMapping("/resend-otp-code")
    @Operation(summary = "Gửi lại OTP (đăng ký nếu email chưa tồn tại, đăng nhập OTP nếu đã tồn tại)")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody EmailRequest request) {
        authService.sendOrResendOtp(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("OTP đã được gửi!", null));
    }

    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản bằng OTP")
    public ResponseEntity<ApiResponse<AuthDataResponse>> register(@Valid @RequestBody VerifyOtpRequest request) {
        AuthDataResponse data = authService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công!", data));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Gửi OTP quên mật khẩu về email")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody EmailRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("OTP đã được gửi đến email của bạn!", null));
    }

    @PostMapping("/update-password")
    @Operation(summary = "Đổi mật khẩu (qua OTP hoặc mật khẩu cũ)")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        authService.updatePassword(request, email);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công!", null));
    }

    @PostMapping("/get-account-status")
    @Operation(summary = "Kiểm tra trạng thái tài khoản theo email")
    public ResponseEntity<ApiResponse<AccountStatusResponse>> getAccountStatus(
            @Valid @RequestBody EmailRequest request) {
        AccountStatusResponse data = authService.getAccountStatus(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/information")
    @Operation(summary = "Lấy thông tin tài khoản hiện tại (cần Token)")
    public ResponseEntity<ApiResponse<UserResponse>> getInformation() {
        UserResponse data = authService.getInformation();
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping("/refresh-token-new")
    @Operation(summary = "Làm mới Access Token bằng Refresh Token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = refreshTokenService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất (Thu hồi Refresh Token, cần Token)")
    public ResponseEntity<LogoutResponse> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.ok(LogoutResponse.builder().success(true).build());
    }
}
