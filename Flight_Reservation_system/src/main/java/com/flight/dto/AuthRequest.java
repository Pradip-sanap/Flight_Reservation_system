package com.flight.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
