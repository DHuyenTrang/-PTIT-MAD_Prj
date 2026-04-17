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
@RequiredArgsConstructor
@Tag(name = "User", description = "Quản lý thông tin người dùng")
public class UserController {

    private final UserService userService;

    // ====== Legacy endpoints (api/v1) ======
    @GetMapping("/api/v1/users/profile")
    @Operation(summary = "Lấy thông tin cá nhân (v1)")
    public ResponseEntity<ApiResponse<UserResponse>> getProfileV1() {
        return ResponseEntity.ok(ApiResponse.success(userService.getMyProfile()));
    }

    @PutMapping("/api/v1/users/profile")
    @Operation(summary = "Cập nhật thông tin cá nhân (v1)")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfileV1(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công!", userService.updateMyProfile(request)));
    }

    @PostMapping("/api/v1/users/change-password")
    @Operation(summary = "Đổi mật khẩu (v1)")
    public ResponseEntity<ApiResponse<String>> changePasswordV1(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công!", null));
    }

    @DeleteMapping("/api/v1/users/account")
    @Operation(summary = "Xóa tài khoản (v1)")
    public ResponseEntity<ApiResponse<String>> deleteAccountV1() {
        userService.deleteMyAccount();
        return ResponseEntity.ok(ApiResponse.success("Đã khóa tài khoản thành công!", null));
    }

    // ====== v5 endpoints ======
    @GetMapping("/v5/api/user/profile-app")
    @Operation(summary = "Lấy thông tin profile (v5, cần Token)")
    public ResponseEntity<ApiResponse<UserResponse>> getProfileApp() {
        return ResponseEntity.ok(ApiResponse.success(userService.getMyProfile()));
    }

    @PutMapping("/v5/api/user/profile-app")
    @Operation(summary = "Cập nhật thông tin profile (v5, cần Token)")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfileApp(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công!", userService.updateMyProfile(request)));
    }

    @PostMapping("/v5/api/user/change-password")
    @Operation(summary = "Đổi mật khẩu (v5, cần Token)")
    public ResponseEntity<ApiResponse<String>> changePasswordV5(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công!", null));
    }

    @DeleteMapping("/v5/api/user/account")
    @Operation(summary = "Xóa tài khoản (v5, cần Token)")
    public ResponseEntity<ApiResponse<String>> deleteAccountV5() {
        userService.deleteMyAccount();
        return ResponseEntity.ok(ApiResponse.success("Đã khóa tài khoản thành công!", null));
    }
}
