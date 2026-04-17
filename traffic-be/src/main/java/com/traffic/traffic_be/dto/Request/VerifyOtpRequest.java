package com.traffic.traffic_be.dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @JsonProperty("otp_code")
    private String otpCode;

    @JsonProperty("display_name")
    private String displayName;

    private String password;
}
