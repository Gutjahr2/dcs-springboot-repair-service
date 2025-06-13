/**
 * (c) DICOS GmbH, 2023
 *
 * $Id$
 */

package de.dicos.springboot.repairservice.restful.services;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.provider.BinaryDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import de.dicos.springboot.repairservice.gen.api.DefaultApi;
import de.dicos.springboot.repairservice.gen.model.RepairRequest;
import de.dicos.springboot.repairservice.restful.dto.ErrorResponse;
import de.dicos.springboot.repairservice.restful.dto.RepairAppointmentResponse;
import de.dicos.springboot.repairservice.restful.exception.ApiResponseException;

/**
 *
 * @author jtibke
 */
@Component
public class AdministrationService
{
	// /////////////////////////////////////////////////////////
	// Class Members
	// /////////////////////////////////////////////////////////

	private DefaultApi api;
	
	private static final Logger log = LoggerFactory.getLogger(AdministrationService.class);

	// /////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////

	public AdministrationService(DefaultApi api) {
	        this.api = api;
	}

	// /////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////

	public RepairAppointmentResponse postRepairRequest(RepairRequest repairRequest)
	{
	    	try {
	    	    api.repairRequestPost(repairRequest);
	    	    return new RepairAppointmentResponse(repairRequest.getPreferredDate());
	    	    
	    	//Vielleicht eigenen Exceptionhandler für Verwaltungssystem implementieren.    
	    	} catch (WebApplicationException e) {
	    	    int status = e.getResponse().getStatus();
	    	    
	    	    String message;
	    	    if (status == 400) {
	    	        message = "Invalid request sent to administration system.";
	    	    } else {
	    	        message = "Administration system returned error " + status + ".";
	    	    }    
	    	    throw new ApiResponseException(ResponseEntity
	    	        .status(status)
	    	        .body(new ErrorResponse(status, message)));
	    	    
	    	} catch (ProcessingException e) {
	    	    Throwable cause = e.getCause();
	    	    if (cause instanceof SocketTimeoutException) {
	    		log.warn("Read timeout occurred", e);
	    		throw new ApiResponseException(ResponseEntity
	    		   .status(HttpStatus.GATEWAY_TIMEOUT)
	    		   .body(new ErrorResponse(504, "Timeout while reading response from administration system")));
	    	    }

	    	    if (cause instanceof ConnectException) {
	    		log.warn("Connection timeout occurred", e);
	    		throw new ApiResponseException(ResponseEntity
	    		   .status(HttpStatus.GATEWAY_TIMEOUT)
	    		   .body(new ErrorResponse(504, "Could not connect to administration system")));
	    	    }
	    	    
	            log.error("Unexpected processing exception", e);
	            throw new ApiResponseException(ResponseEntity
	               .status(HttpStatus.INTERNAL_SERVER_ERROR)
	               .body(new ErrorResponse(500, "Unexpected communication error")));  
	    	}    
	}

	// /////////////////////////////////////////////////////////
	// Inner Classes
	// /////////////////////////////////////////////////////////


}
