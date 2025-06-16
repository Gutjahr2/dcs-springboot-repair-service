package de.dicos.springboot.repairservice.restful.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Collections;

import javax.ws.rs.ProcessingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.dicos.springboot.repairservice.gen.api.DefaultApi;
import de.dicos.springboot.repairservice.restful.dto.RepairAppointmentResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairOperationDto;
import de.dicos.springboot.repairservice.restful.dto.RepairRequestDto;
import de.dicos.springboot.repairservice.restful.exception.RepairServiceException;
import de.dicos.springboot.repairservice.restful.model.RepairAction;

public class AdministrationServiceTest {

    @Mock
    private DefaultApi defaultApi;

    @InjectMocks
    private AdministrationService service;

    @BeforeEach
    void init() {
	MockitoAnnotations.openMocks(this);
	service = new AdministrationService(defaultApi);
    }

    private RepairRequestDto createValidRequest() {
	RepairOperationDto repairOperation = new RepairOperationDto();
	repairOperation.setDescription(RepairAction.BREMSBELAGWECHSEL);
	repairOperation.setPriceEstimation(335.21);

	RepairRequestDto requestDto = new RepairRequestDto();
	requestDto.setCustomerNumber(1233);
	requestDto.setNumberPlate("RÜD-MG-6");
	requestDto.setCarModel("Audi A3");
	requestDto.setPreferredDate("01.09.2026");
	requestDto.setRepairOperations(Collections.singletonList(repairOperation));
	return requestDto;
    }

    @Test
    void testSuccessfulRequest() {
	RepairRequestDto requestDto = createValidRequest();
	doNothing().when(defaultApi).repairRequestPost(any());

	RepairAppointmentResponseDto response = service.postRepairRequest(requestDto);
	assertEquals("01.09.2026", response.getRepairDate());
    }

    @Test
    void testSocketTimeout() {
	RepairRequestDto requestDto = createValidRequest();
	ProcessingException exception = new ProcessingException(new SocketTimeoutException("Timeout"));
	doThrow(exception).when(defaultApi).repairRequestPost(any());

	RepairServiceException ex = assertThrows(RepairServiceException.class,
		() -> service.postRepairRequest(requestDto));
	assertEquals(504, ex.getResponse().getStatusCodeValue());
    }

    @Test
    void testConnectException() {
	RepairRequestDto requestDto = createValidRequest();
	ProcessingException exception = new ProcessingException(new ConnectException("Connect fail"));
	doThrow(exception).when(defaultApi).repairRequestPost(any());

	RepairServiceException ex = assertThrows(RepairServiceException.class,
		() -> service.postRepairRequest(requestDto));
	assertEquals(504, ex.getResponse().getStatusCodeValue());
    }

    @Test
    void testUnexpectedProcessingException() {
	RepairRequestDto requestDto = createValidRequest();
	ProcessingException exception = new ProcessingException("Unknown error");
	doThrow(exception).when(defaultApi).repairRequestPost(any());

	RepairServiceException ex = assertThrows(RepairServiceException.class,
		() -> service.postRepairRequest(requestDto));
	assertEquals(500, ex.getResponse().getStatusCodeValue());
    }

}
