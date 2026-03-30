package com.flight.controller;


import com.flight.dto.UserDto;
import com.flight.dto.response.UserResponse;
import com.flight.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> registerNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("Request received for registering new User");
        return new ResponseEntity<>(userService.registerNewUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int userId){
        log.info("Request received for fetching user for userId={}", userId);
        UserResponse user = userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<String> isValidUser(@PathVariable int userId){
        log.info("Request received for validating existing User");
        return new ResponseEntity<>(userService.validateUserId(userId), HttpStatus.OK);
    }




}
