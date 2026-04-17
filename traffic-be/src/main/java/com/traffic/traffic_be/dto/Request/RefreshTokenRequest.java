package com.traffic.traffic_be.dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
}
