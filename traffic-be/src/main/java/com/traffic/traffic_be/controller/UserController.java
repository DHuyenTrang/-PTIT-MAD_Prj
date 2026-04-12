package com.traffic.traffic_be.controller;

import com.traffic.traffic_be.dto.Request.ChangePasswordRequest;
import com.traffic.traffic_be.dto.Request.UpdateProfileRequest;
import com.traffic.traffic_be.dto.Response.ApiResponse;
import com.traffic.traffic_be.dto.Response.UserResponse;
import com.traffic.traffic_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Quản lý thông tin người dùng")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Lấy thông tin cá nhân")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success(userService.getMyProfile()));
    }

    @PutMapping("/profile")
    @Operation(summary = "Cập nhật thông tin cá nhân (Cần Token)")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công!", userService.updateMyProfile(request)));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu chủ động (Cần Token)")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công!", null));
    }

    @DeleteMapping("/account")
    @Operation(summary = "Khóa/Xóa mềm tài khoản của tôi (Cần Token)")
    public ResponseEntity<ApiResponse<String>> deleteAccount() {
        userService.deleteMyAccount();
        return ResponseEntity.ok(ApiResponse.success("Đã khóa tài khoản thành công!", null));
    }
}
