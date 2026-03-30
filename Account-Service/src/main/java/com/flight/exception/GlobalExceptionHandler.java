package com.flight.exception;

import com.flight.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        "Payment not found",
                        HttpStatus.NOT_FOUND,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RefundProcessingException.class)
    public ResponseEntity<ErrorResponse> handleRefundProcessing(RefundProcessingException ex, HttpServletRequest request){
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRefundProcessing(AccountNotFoundException ex, HttpServletRequest request){
        log.error("");
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRefundProcessing(AccountAlreadyExistsException ex, HttpServletRequest request){
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        HttpStatus.CONFLICT,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidUserDetailsException.class)
    public ResponseEntity<ErrorResponse> handleRefundProcessing(InvalidUserDetailsException ex, HttpServletRequest request){
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        HttpStatus.CONFLICT,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UserAccountAlreadyPresentException.class)
    public ResponseEntity<ErrorResponse> handleRefundProcessing(UserAccountAlreadyPresentException ex, HttpServletRequest request){
        return new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        HttpStatus.CONFLICT,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ),
                HttpStatus.CONFLICT
        );
    }
}
