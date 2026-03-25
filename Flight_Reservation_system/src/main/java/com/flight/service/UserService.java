package com.flight.service;

import com.flight.dto.UserDto;
import com.flight.dto.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserResponse registerNewUser(UserDto userDto);
    UserResponse getUser(int userId);
    UserDetails loadUserByUsername(String username);

    boolean validateUserId(Integer userId);

}
