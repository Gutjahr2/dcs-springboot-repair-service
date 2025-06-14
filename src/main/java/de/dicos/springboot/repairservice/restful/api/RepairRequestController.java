/**
 * (c) DICOS GmbH, 2023
 *
 * $Id$
 */

package de.dicos.springboot.repairservice.restful.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairAppointmentResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairRequestDto;
import de.dicos.springboot.repairservice.restful.services.AdministrationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author jtibke
 */
@NoArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/rest/repair-request")
public class RepairRequestController
{
	// /////////////////////////////////////////////////////////
	// Class Members
	// /////////////////////////////////////////////////////////

	@Autowired
	private AdministrationService administrationService;
	
	private static final Logger log = LoggerFactory.getLogger(RepairRequestController.class);

	// /////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////


	// /////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////

	@Operation(summary = "Create a new car repair request and send it to the administration system",
	               description = "This endpoint receives a repair request with customer details and the necessary repair operations, and sends it to the administration system.")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Car repair request created successfully", 
	                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RepairAppointmentResponseDto.class))),
	        @ApiResponse(responseCode = "400", description = "Bad request, invalid input data", 
	                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
	        @ApiResponse(responseCode = "500", description = "Internal server error, failed to process the request", 
	                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
	    })
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendRepairRequest(@Valid @RequestBody RepairRequestDto request)
		throws Exception
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(administrationService.postRepairRequest(request));
	}

	// /////////////////////////////////////////////////////////
	// Inner Classes
	// /////////////////////////////////////////////////////////


}