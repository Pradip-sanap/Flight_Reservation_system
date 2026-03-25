package com.flight.dto;

import com.flight.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int userId;
    private String name;
    private UserRole role;
}
