package com.flight.service.impl;

import com.flight.dto.UserDto;
import com.flight.dto.response.UserResponse;
import com.flight.exception.UserNotFoundException;
import com.flight.model.User;
import com.flight.repository.UserRepository;
import com.flight.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Method override from UserDetailsService interface.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userObj = userRepository.findUserByName(username).orElseThrow(() ->
                                                                    new UserNotFoundException("User Not Found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(userObj.getName())
                .password(userObj.getPassword())
                .build();
    }

    @Override
    public String validateUserId(Integer userId) {
        log.info("Validating user with userId={}", userId);
        boolean exists = userRepository.existsById(userId);
        if(exists){
            log.info("User is valid for userId={}", userId);
            return "User Exists";
        }
        log.error("User is not validated for userId={}", userId);
        return "User details not found";
    }

    @Override
    public UserResponse registerNewUser(UserDto userDto) {
        log.info("Request received for registering new User");
        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        //Password Encoded
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        log.debug("Password encoded successfully");

        //save to db
        User savedUser =  userRepository.save(user);
        log.info("User Register successfully");
        return mapToResponse(savedUser);
    }


    @Override
    public UserResponse getUser(int userId) {
        log.debug("Request come in user Service");
        User user = userRepository.findById(userId).orElseThrow(()->{
            log.error("User not found with userId={}", userId);
            return new UserNotFoundException("User Not found");
        });
        log.info("Request is successful for fetching user with userId={}", userId);
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user){
        return new UserResponse(user.getUserId(),user.getName(),user.getRole());
    }


}
