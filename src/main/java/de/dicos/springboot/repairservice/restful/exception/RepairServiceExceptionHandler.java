package de.dicos.springboot.repairservice.restful.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;

@RestControllerAdvice
public class RepairServiceExceptionHandler {

    @ExceptionHandler(RepairServiceException.class)
    public ResponseEntity<?> handleApiResponseException(RepairServiceException ex) {
	return ex.getResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
	String message = ex.getBindingResult().getFieldErrors().stream()
		.map(field -> field.getField() + ": " + field.getDefaultMessage()).collect(Collectors.joining(", "));

	return ResponseEntity.badRequest().body(new ErrorResponseDto(400, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
	Throwable cause = ex.getCause();

	if (cause instanceof InvalidFormatException) {
	    return ResponseEntity.badRequest().body(new ErrorResponseDto(400, "Invalid format in request body"));
	}

	return ResponseEntity.badRequest().body(new ErrorResponseDto(400, "Request body is missing or invalid"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception ex) {
	return ResponseEntity.internalServerError().body(new ErrorResponseDto(500, "Unexpected internal server error"));
    }

}