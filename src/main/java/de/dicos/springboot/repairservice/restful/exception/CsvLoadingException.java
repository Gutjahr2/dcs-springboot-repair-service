package de.dicos.springboot.repairservice.restful.exception;

public class CsvLoadingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CsvLoadingException(String message) {
	super(message);
    }

    public CsvLoadingException(String message, Throwable cause) {
	super(message, cause);
    }
}
