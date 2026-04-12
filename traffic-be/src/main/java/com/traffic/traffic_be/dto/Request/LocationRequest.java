package com.traffic.traffic_be.dto.Request;

import lombok.Data;

@Data
public class LocationRequest {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
