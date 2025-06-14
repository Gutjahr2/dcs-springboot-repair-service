package de.dicos.springboot.repairservice.restful.exception;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;

public class AdministrationSystemResponseExceptionMapper implements ResponseExceptionMapper<Throwable> {

    private static final Logger log = LoggerFactory.getLogger(AdministrationSystemResponseExceptionMapper.class);

    @Override
    public Throwable fromResponse(Response response) {
        int status = response.getStatus();
        String message;

        if(status<300) {
            return null;
        }
        switch (status) {
            case 400:
                message = "Invalid request sent to the administration system.";
                break;
            case 401:
                message = "Unauthorized";
                break;
            case 403:
                message = "Forbidden";
                break;
            case 404:
                message = "NOT FOUND";
                break;
            case 500:
                message = "Internal server error in administration system.";
                break;
            default:
                message = "Unhandled HTTP status from administration system: " + status;
                status = 500; // catch-all fallback
        }

        log.error("Administration system responded with HTTP {} - {}", status, message);

        return new ApiResponseException(
            ResponseEntity.status(status).body(new ErrorResponseDto(status, message))
        );
    }

}
