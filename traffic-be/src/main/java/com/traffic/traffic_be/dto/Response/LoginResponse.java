package com.traffic.traffic_be.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;

    private String email;

    @JsonProperty("full_name")
    private String fullName;
}
