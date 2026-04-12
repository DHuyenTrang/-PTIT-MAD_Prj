package com.traffic.traffic_be.dto.Request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String phone;
    private String avatarUrl;
    // Không cho phép đổi username và email ở API này để bảo mật
}