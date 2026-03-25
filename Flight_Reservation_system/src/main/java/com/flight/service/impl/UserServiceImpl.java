package com.flight.service.impl;

import com.flight.dto.UserDto;
import com.flight.dto.UserResponse;
import com.flight.exception.UserNotFoundException;
import com.flight.model.User;
import com.flight.repository.UserRepository;
import com.flight.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Method override from UserDetailsService interface.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userObj = userRepository.findUserByName(username).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(userObj.getName())
                .password(userObj.getPassword())
                .build();
    }

    @Override
    public boolean validateUserId(Integer userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public UserResponse registerNewUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        //Password Encoded
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));

        //save to db
        User savedUser =  userRepository.save(user);

        return mapToResponse(savedUser);
    }


    @Override
    public UserResponse getUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                                                new UserNotFoundException("User Not found with id: "+ userId));
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user){
        return new UserResponse(user.getUserId(),user.getName(),user.getRole());
    }


}
