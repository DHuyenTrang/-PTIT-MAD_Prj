package com.traffic.traffic_be.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginWithPasswordRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
