package com.project.code.Controller;

// 1. Spring Web & HTTP Imports
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 2. Java Utility Imports
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 2. Define the handleJsonParseException Method
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Forces the HTTP response to be 400 Bad Request
    public Map<String, Object> handleJsonParseException() {
        
        Map<String, Object> response = new HashMap<>();
        
        // Strictly matching the instruction's requested string
        response.put("message", "Invalid input: The data provided is not valid.");
        
        return response;
    }
}