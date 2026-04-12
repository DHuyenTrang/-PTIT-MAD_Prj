package com.traffic.traffic_be.dto.Response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter @Builder
public class ErrorResponse {
    private int status;
    private String message;
    private String details;
    private LocalDateTime timestamp;
}
