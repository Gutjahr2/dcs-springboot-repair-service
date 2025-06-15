/**
 * (c) DICOS GmbH, 2023
 *
 * $Id$
 */

package de.dicos.springboot.repairservice.restful.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationRequestDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationResponseDto;
import de.dicos.springboot.repairservice.restful.services.RepairEstimationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller for calculating estimated repair costs based on car model and
 * required operations.
 * 
 *
 */
@RestController
@RequestMapping(path = "/rest/repair-estimation")
public class RepairEstimationController {

    @Autowired
    private RepairEstimationService repairEstimationService;

    private static final Logger log = LoggerFactory.getLogger(RepairEstimationController.class);

    @Operation(summary = "Estimate repair costs based on car model and operations", description = "This endpoint receives the car model and a list of repair operations, looks up price estimates from a pricing table, and returns the total estimated cost.")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Cost estimation calculated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RepairEstimationResponseDto.class))),
	    @ApiResponse(responseCode = "400", description = "Invalid input data provided", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))),
	    @ApiResponse(responseCode = "500", description = "Internal server error during cost estimation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))) })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> estimateRepairCost(@Valid @RequestBody RepairEstimationRequestDto request)
	    throws Exception {
	return ResponseEntity.status(HttpStatus.CREATED).body(repairEstimationService.estimateCosts(request));
    }
}
