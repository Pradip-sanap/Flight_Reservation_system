package com.flight.controller;

import com.flight.dto.AuthRequest;
import com.flight.dto.AuthResponse;
import com.flight.jwt.JwtService;
import com.flight.model.RefreshToken;
import com.flight.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequestObj){
        Authentication authenticated = manager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestObj.getUsername(),
                authRequestObj.getPassword()
        ));
        if(authenticated.isAuthenticated()){
            AuthResponse resp = new AuthResponse();

            String newAccessToken =  jwtService.generateAccessToken(authRequestObj.getUsername());
            resp.setAccessToken(newAccessToken);

            String newRefreshToken = jwtService.generateRefreshToken(authRequestObj.getUsername());
            resp.setRefressToken(newRefreshToken);
            //save refreshToken to db for future verification
            refreshTokenService.saveRefreshTokenInDB(authRequestObj.getUsername(), newRefreshToken);

            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        throw new RuntimeException("Invalid");
    }

    @GetMapping("/refresh")
    public String refreshTheToken(@RequestHeader String refreshToken){
        String token = refreshToken.substring(7);
        RefreshToken refreshTokenObj = refreshTokenService.verifyRefreshToken(token);
        String username = refreshTokenObj.getUsername();

        // Generate new access token for valid refresh token
        String newAccessToken = jwtService.generateAccessToken(username);

        return "Access Token: "+ newAccessToken;
    }
}
