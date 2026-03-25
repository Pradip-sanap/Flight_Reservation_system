package com.flight.jwt;

import com.flight.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Lazy
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
       // This if block contain code for allowing new user registration api. This api did not need token during registration
        String path = request.getServletPath();
        if (path.equals("/api/user") && request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

//         System.out.println("JwtFilter called");
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null && jwtService.validateToken(token)) {
            System.out.println("Valid JWT Token");

            String username = jwtService.extractUsername(token);            // extract username from token
            UserDetails user = userService.loadUserByUsername(username);    // get userObj from db using username
            UsernamePasswordAuthenticationToken a = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(a);
        } else {
            System.out.println("Invalid JWT Token");
        }

        filterChain.doFilter(request, response);
    }
}
