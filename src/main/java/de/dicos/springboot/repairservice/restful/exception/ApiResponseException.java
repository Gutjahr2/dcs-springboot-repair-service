package de.dicos.springboot.repairservice.restful.exception;

import org.springframework.http.ResponseEntity;

public class ApiResponseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final ResponseEntity<?> response;

    public ApiResponseException(ResponseEntity<?> response) {
        super();
        this.response = response;
    }

    public ApiResponseException(String message, ResponseEntity<?> response) {
        super(message);
        this.response = response;
    }

    public ResponseEntity<?> getResponse() {
        return response;
    }
}