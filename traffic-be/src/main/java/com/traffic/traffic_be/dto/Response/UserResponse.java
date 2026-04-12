package com.traffic.traffic_be.dto.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private boolean isActive;
}