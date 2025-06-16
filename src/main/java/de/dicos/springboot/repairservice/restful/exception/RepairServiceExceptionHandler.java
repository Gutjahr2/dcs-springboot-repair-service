package de.dicos.springboot.repairservice.restful.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;

/**
 * Global exception handler for REST endpoints
 */
@RestControllerAdvice
public class RepairServiceExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RepairServiceExceptionHandler.class);

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
	    InvalidFormatException inavildFormatException = (InvalidFormatException) cause;
	    Class<?> targetType = inavildFormatException.getTargetType();
	    Object value = inavildFormatException.getValue();

	    if (targetType.isEnum()) {
		String allowedValues = String.join(", ", Arrays.stream(targetType.getEnumConstants())
			.map(Object::toString).collect(Collectors.toList()));
		String message = String.format("Invalid value '%s' for enum %s", value, targetType.getSimpleName(),
			allowedValues);
		log.warn("Enum deserialization failed: {}", message);
		return ResponseEntity.badRequest().body(new ErrorResponseDto(400, message));
	    }
	    log.warn("Invalid format in request body: {}", inavildFormatException.getMessage());
	    return ResponseEntity.badRequest().body(new ErrorResponseDto(400, "Invalid format in request body"));
	}
	log.warn("Invalid request body: {}", ex.getMessage());
	return ResponseEntity.badRequest().body(new ErrorResponseDto(400, "Request body is missing or invalid"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception ex) {
	log.error("Unhandled exception occurred: {}", ex.getMessage());
	return ResponseEntity.internalServerError().body(new ErrorResponseDto(500, "Unexpected internal server error"));
    }

}