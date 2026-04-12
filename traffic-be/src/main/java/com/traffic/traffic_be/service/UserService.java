package com.traffic.traffic_be.service;
import com.traffic.traffic_be.dto.Request.ChangePasswordRequest;
import com.traffic.traffic_be.dto.Request.UpdateProfileRequest;
import com.traffic.traffic_be.dto.Response.UserResponse;

public interface UserService {
    UserResponse getMyProfile();

    // Đổi tên thành updateMyProfile cho khớp với Impl
    UserResponse updateMyProfile(UpdateProfileRequest request);

    // Bổ sung 2 hàm mới
    void changePassword(ChangePasswordRequest request);

    void deleteMyAccount();
}