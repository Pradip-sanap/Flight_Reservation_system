package com.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
    private String message;
    private String path;

}
