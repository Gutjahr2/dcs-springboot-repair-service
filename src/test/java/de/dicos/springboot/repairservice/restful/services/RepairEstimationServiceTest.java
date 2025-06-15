package de.dicos.springboot.repairservice.restful.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationRequestDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationResponseDto;
import de.dicos.springboot.repairservice.restful.exception.RepairServiceException;
import de.dicos.springboot.repairservice.restful.model.CarModel;
import de.dicos.springboot.repairservice.restful.model.RepairAction;

public class RepairEstimationServiceTest {

    private RepairEstimationService service;

    @BeforeEach
    void setup() {
	service = new RepairEstimationService();
	service.loadPriceDataFromResourceForTest("test_price_estimations.csv");
    }

    @Test
    void testSuccessfulEstimation() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A3);
	request.setRepairActions(Arrays.asList(RepairAction.OELWECHSEL, RepairAction.BREMSBELAGWECHSEL));

	RepairEstimationResponseDto response = service.estimateCosts(request);

	assertEquals(741.05, response.getEstimatedPrice());
    }

    @Test
    void testUnknownRepairAction_ThrowsRepairServiceException() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A4);
	request.setRepairActions(Arrays.asList(RepairAction.LUFTFILTERWECHSEL));

	RepairServiceException exception = assertThrows(RepairServiceException.class,
		() -> service.estimateCosts(request));

	ResponseEntity<?> response = exception.getResponse();
	assertEquals(500, response.getStatusCodeValue());
	assertEquals("No repair actions with price information found for car model: Audi A4",
		((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    void testRepairActionsNull_ThrowsRepairServiceException() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A3);
	request.setRepairActions(null);

	RepairServiceException exception = assertThrows(RepairServiceException.class,
		() -> service.estimateCosts(request));

	ResponseEntity<?> response = exception.getResponse();
	assertEquals(400, response.getStatusCodeValue());
	assertEquals("At least one repair action is required", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    void testEmptyRepairActionsList_ThrowsRepairServiceException() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A3);
	request.setRepairActions(Arrays.asList());

	RepairServiceException exception = assertThrows(RepairServiceException.class,
		() -> service.estimateCosts(request));

	ResponseEntity<?> response = exception.getResponse();
	assertEquals(400, response.getStatusCodeValue());
	assertEquals("At least one repair action is required", ((ErrorResponseDto) response.getBody()).getMessage());
    }

    @Test
    void testCarModelNull_ThrowsRepairServiceException() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(null);
	request.setRepairActions(Arrays.asList(RepairAction.LUFTFILTERWECHSEL));

	RepairServiceException exception = assertThrows(RepairServiceException.class,
		() -> service.estimateCosts(request));

	ResponseEntity<?> response = exception.getResponse();
	assertEquals(400, response.getStatusCodeValue());
	assertEquals("Car model is required", ((ErrorResponseDto) response.getBody()).getMessage());
    }

}
