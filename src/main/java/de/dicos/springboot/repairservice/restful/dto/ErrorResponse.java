package de.dicos.springboot.repairservice.restful.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response object")
public class ErrorResponse {

    @Schema(description = "Http Statuscode", example = "400")
    private int code;
    
    @Schema(description = "Error message", example = "Invalid input data")
    private String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}