/**
 * (c) DICOS GmbH, 2023
 *
 * $Id$
 */

package de.dicos.springboot.repairservice.restful.services;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.dicos.springboot.repairservice.gen.api.DefaultApi;
import de.dicos.springboot.repairservice.gen.model.RepairOperation;
import de.dicos.springboot.repairservice.gen.model.RepairRequest;
import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairAppointmentResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairOperationDto;
import de.dicos.springboot.repairservice.restful.dto.RepairRequestDto;
import de.dicos.springboot.repairservice.restful.exception.RepairServiceException;

/**
 *
 * @author jtibke
 */
@Component
public class AdministrationService {
    // /////////////////////////////////////////////////////////
    // Class Members
    // /////////////////////////////////////////////////////////

    private DefaultApi api;

    private static final Logger log = LoggerFactory.getLogger(AdministrationService.class);

    @Value("${administration.mock.enabled}")
    private boolean mockEnabled;

    @Value("${administration.mock.response-status}")
    private int mockResponseStatus;

    public AdministrationService(DefaultApi api) {
	this.api = api;
    }

    /**
     * Sends a repair request to the administration system or simulates a response
     * in mock mode
     *
     * @param repairRequest
     * @return RepairAppointmentResponseDto
     * @throws RepairServiceException
     */
    public RepairAppointmentResponseDto postRepairRequest(RepairRequestDto repairRequest) {
	if (mockEnabled) {
	    log.info("MOCK MODE enabled – simulating administration system response with status {}",
		    mockResponseStatus);
	    return postRepairRequestMock(repairRequest);
	}
	try {
	    RepairRequest request = mapToRepairRequest(repairRequest);
	    api.repairRequestPost(request);
	    return new RepairAppointmentResponseDto(repairRequest.getPreferredDate());

	    // This exception is thrown and logged by the
	    // AdministrationSystemResponseExceptionMapper
	} catch (RepairServiceException e) {
	    throw e;
	} catch (ProcessingException e) {
	    Throwable cause = e.getCause();
	    if (cause instanceof SocketTimeoutException) {
		log.warn("Read timeout occurred", e);
		throw new RepairServiceException(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
			.body(new ErrorResponseDto(504, "Timeout while reading response from administration system")));
	    }

	    if (cause instanceof ConnectException) {
		log.warn("Connection timeout occurred", e);
		throw new RepairServiceException(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
			.body(new ErrorResponseDto(504, "Could not connect to administration system")));
	    }

	    log.error("Unexpected processing exception", e);
	    throw new RepairServiceException(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		    .body(new ErrorResponseDto(500, "Unexpected communication error")));
	}
    }

    private RepairAppointmentResponseDto postRepairRequestMock(RepairRequestDto repairRequest) {
	if (mockResponseStatus == 201) {
	    log.info("Mock mode: Simulating successful response");
	    return new RepairAppointmentResponseDto(repairRequest.getPreferredDate());
	} else {
	    log.info("Mock mode: Simulating error response with status {}", mockResponseStatus);
	    throw new RepairServiceException(ResponseEntity.status(mockResponseStatus)
		    .body(new ErrorResponseDto(mockResponseStatus, "Simulated error message in mock mode")));
	}
    }

    private RepairRequest mapToRepairRequest(RepairRequestDto dto) {
	log.debug("Mapping RepairRequestDto to RepairRequest");
	RepairRequest request = new RepairRequest();
	request.setCustomerNumber(dto.getCustomerNumber());
	request.setCarModel(dto.getCarModel());
	request.setNumberPlate(dto.getNumberPlate());
	request.setPreferredDate(dto.getPreferredDate());

	List<RepairOperation> ops = new ArrayList<>();
	for (RepairOperationDto opDto : dto.getRepairOperations()) {
	    RepairOperation op = new RepairOperation();
	    op.setDescription(opDto.getDescription().getCsvLabel());
	    op.setPriceEstimation(opDto.getPriceEstimation());
	    ops.add(op);
	}
	request.setRepairOperations(ops);

	return request;
    }
}
