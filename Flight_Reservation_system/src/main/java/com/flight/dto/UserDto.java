package com.flight.dto;

import com.flight.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank
    private String name;

    @NotBlank
    private String password;

    private UserRole role;
}
