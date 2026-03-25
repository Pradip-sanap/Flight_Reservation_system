package com.flight.controller;


import com.flight.dto.UserDto;
import com.flight.dto.UserResponse;
import com.flight.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> registerNewUser(@Valid @RequestBody UserDto userDto)
    {
//        System.out.println("Log 1"+ userDto);

//        System.out.println("Log 4"+newUser);
        return new ResponseEntity<>(userService.registerNewUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int userId){
        UserResponse user = userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
