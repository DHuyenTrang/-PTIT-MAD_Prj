package com.traffic.traffic_be.dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class DeleteLicensePlateRequest {
    @NotEmpty
    @JsonProperty("license_plates")
    private List<String> licensePlates;
}
