package com.traffic.traffic_be.dto.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogoutResponse {
    private boolean success;
}
