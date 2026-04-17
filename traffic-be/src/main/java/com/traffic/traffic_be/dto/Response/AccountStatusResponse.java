package com.traffic.traffic_be.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountStatusResponse {
    @JsonProperty("has_password")
    private boolean hasPassword;

    @JsonProperty("is_registered")
    private boolean isRegistered;
}
