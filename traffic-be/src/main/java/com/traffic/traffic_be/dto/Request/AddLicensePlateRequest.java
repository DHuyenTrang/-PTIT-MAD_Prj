package com.traffic.traffic_be.dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddLicensePlateRequest {
    @NotBlank
    @JsonProperty("license_plate")
    private String licensePlate;
}
