package de.dicos.springboot.repairservice.restful.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairAppointmentResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairOperationDto;
import de.dicos.springboot.repairservice.restful.dto.RepairRequestDto;
import de.dicos.springboot.repairservice.restful.model.RepairAction;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "administration.mock.enabled=true", "administration.mock.response-status=201" })
public class RepairRequestControllerTest {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
	return "http://localhost:" + port + contextPath + path;
    }

    private HttpHeaders authHeaders() {
	HttpHeaders headers = new HttpHeaders();
	headers.setBasicAuth("testuser", "test");
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	return headers;
    }

    private RepairRequestDto createValidRequestPayload() {
	RepairRequestDto request = new RepairRequestDto();
	request.setCustomerNumber(1234);
	request.setCarModel("Audi A3");
	request.setNumberPlate("RÜD-MG-6");
	request.setPreferredDate("01.01.2026");
	request.setRepairOperations(Arrays.asList(new RepairOperationDto(RepairAction.OELWECHSEL, 405.84)));
	return request;
    }

    @Test
    void whenValidRepairRequest_thenReturns201() {
	RepairRequestDto request = createValidRequestPayload();
	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<RepairAppointmentResponseDto> response = restTemplate
		.postForEntity(url("/rest/repair-request/create"), entity, RepairAppointmentResponseDto.class);

	assertEquals(HttpStatus.CREATED, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(request.getPreferredDate(), response.getBody().getRepairDate());
    }

    @Test
    void whenMissingAuth_thenReturns401() {
	RepairRequestDto request = createValidRequestPayload();

	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, headers);

	ResponseEntity<String> response = restTemplate.postForEntity(url("/rest/repair-request/create"), entity,
		String.class);

	assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

    @Test
    void whenCustomerNumberIsNull_thenReturns400() {
	RepairRequestDto request = createValidRequestPayload();
	request.setCustomerNumber(null);

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("customerNumber: Customer number is required", response.getBody().getMessage());
    }

    @Test
    void whenNumberPlateIsBlank_thenReturns400() {
	RepairRequestDto request = createValidRequestPayload();
	request.setNumberPlate("");

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("numberPlate: Number plate is required", response.getBody().getMessage());
    }

    @Test
    void whenRepairOperationDescriptionIsNull_thenReturns400() {
	RepairRequestDto request = createValidRequestPayload();
	request.getRepairOperations().get(0).setDescription(null);

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("repairOperations[0].description: Repair operation description is required",
		response.getBody().getMessage());
    }

    @Test
    void whenRepairOperationPriceIsNull_thenReturns400() {
	RepairRequestDto request = createValidRequestPayload();
	request.getRepairOperations().get(0).setPriceEstimation(null);

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("repairOperations[0].priceEstimation: Repair operation price estimation is required",
		response.getBody().getMessage());
    }

    @Test
    void whenRepairOperationsNull_thenReturns400() {
	RepairRequestDto request = createValidRequestPayload();
	request.setRepairOperations(null);

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("repairOperations: At least one repair operation is required", response.getBody().getMessage());
    }

    @Test
    void whenRepairOperationsEmpty_thenReturns400() {
	RepairRequestDto request = createValidRequestPayload();
	request.setRepairOperations(Collections.emptyList());

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("repairOperations: At least one repair operation is required", response.getBody().getMessage());
    }

    @Test
    void whenInvalidJson_thenReturns400() {
	HttpHeaders headers = authHeaders();
	HttpEntity<String> entity = new HttpEntity<>("{invalidJson", headers);

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("Request body is missing or invalid", response.getBody().getMessage());
    }

    @Test
    void whenRequestBodyIsNull_thenReturns400() {
	HttpHeaders headers = authHeaders();
	HttpEntity<String> entity = new HttpEntity<>(null, headers);

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("Request body is missing or invalid", response.getBody().getMessage());
    }
}
