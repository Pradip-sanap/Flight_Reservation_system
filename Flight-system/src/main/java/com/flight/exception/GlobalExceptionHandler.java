package com.flight.exception;

import com.flight.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //handle userNotFound
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUserNotFound(UserNotFoundException ex, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("NOT_FOUND")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //handle userNotFound
    @ExceptionHandler(SeatsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> seatsNotAvailable(SeatsNotAvailableException ex, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("CONFLICT")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFlightNotFound(FlightNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("NOT_FOUND")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleFlightNotFound(RefreshTokenExpiredException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Unauthorized")
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> handleFlightNotFound(InvalidAmountException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Unauthorized")
                .message(ex.getMessage())
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }

    @ExceptionHandler(InsufficientFundException.class)
    public ResponseEntity<ErrorResponse> handleFlightNotFound(InsufficientFundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Insufficient Balance")
                .message(ex.getMessage())
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(PaymentUnsuccessfulException.class)
    public ResponseEntity<ErrorResponse> handleFlightNotFound(PaymentUnsuccessfulException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Unauthorized")
                .message(ex.getMessage())
                .status(HttpStatus.PAYMENT_REQUIRED)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.PAYMENT_REQUIRED);
    }

    // Optional: handle other exceptions globally
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("INTERNAL_SERVER_ERROR")
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDuplicate(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User already exists or constraint violated");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDbError(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Database error occurred");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshToken(InvalidRefreshTokenException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
