package de.dicos.springboot.repairservice.restful.exception;

import org.springframework.http.ResponseEntity;

public class RepairServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final ResponseEntity<?> response;

    public RepairServiceException(ResponseEntity<?> response) {
        super();
        this.response = response;
    }

    public RepairServiceException(String message, ResponseEntity<?> response) {
        super(message);
        this.response = response;
    }

    public ResponseEntity<?> getResponse() {
        return response;
    }
}