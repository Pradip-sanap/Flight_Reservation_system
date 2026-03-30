package com.flight.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refressToken;
}
