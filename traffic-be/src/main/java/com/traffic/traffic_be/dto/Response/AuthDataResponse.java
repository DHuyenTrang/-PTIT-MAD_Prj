package com.traffic.traffic_be.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDataResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expire_time")
    private Long expireTime;

    @JsonProperty("customer_id")
    private Integer customerId;

    @JsonProperty("display_name")
    private String displayName;

    private String email;
}
