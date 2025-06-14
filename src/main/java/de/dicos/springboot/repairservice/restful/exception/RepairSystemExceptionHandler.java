package de.dicos.springboot.repairservice.restful.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;

@RestControllerAdvice
public class RepairSystemExceptionHandler {

    @ExceptionHandler(ApiResponseException.class)
    public ResponseEntity<?> handleApiResponseException(ApiResponseException ex) {
        return ex.getResponse();
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(field -> field.getField() + ": " + field.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(new ErrorResponseDto(400, message));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponseDto(500, "Unexpected internal server error"));
    }
    
}