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

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationRequestDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationResponseDto;
import de.dicos.springboot.repairservice.restful.model.CarModel;
import de.dicos.springboot.repairservice.restful.model.RepairAction;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RepairEstimationControllerTest {

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

    @Test
    void whenValidRequest_thenReturns201AndCorrectPrice() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A3);
	request.setRepairActions(Arrays.asList(RepairAction.OELWECHSEL, RepairAction.BREMSBELAGWECHSEL));

	HttpEntity<RepairEstimationRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<RepairEstimationResponseDto> response = restTemplate
		.postForEntity(url("/rest/repair-estimation/create"), entity, RepairEstimationResponseDto.class);

	assertEquals(HttpStatus.CREATED, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(741.05, response.getBody().getEstimatedPrice());
    }

    @Test
    void whenNoAuth_thenReturns401() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A3);
	request.setRepairActions(Arrays.asList(RepairAction.OELWECHSEL));

	HttpHeaders headers = new HttpHeaders(); // keine Auth
	headers.setContentType(MediaType.APPLICATION_JSON);

	HttpEntity<RepairEstimationRequestDto> entity = new HttpEntity<>(request, headers);

	ResponseEntity<String> response = restTemplate.postForEntity(url("/rest/repair-estimation/create"), entity,
		String.class);

	assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenRepairActionIsNull_thenReturns400() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A3);
	request.setRepairActions(null);

	HttpEntity<RepairEstimationRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-estimation/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("repairActions: At least one repair action is required", response.getBody().getMessage());
    }

    @Test
    void whenRepairActionsIsEmpty_thenReturns400() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(CarModel.AUDI_A3);
	request.setRepairActions(Collections.emptyList());

	HttpEntity<RepairEstimationRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-estimation/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("repairActions: At least one repair action is required", response.getBody().getMessage());
    }

    @Test
    void whenCarModelIsNull_thenReturns400() {
	RepairEstimationRequestDto request = new RepairEstimationRequestDto();
	request.setCarModel(null);
	request.setRepairActions(Arrays.asList(RepairAction.OELWECHSEL));

	HttpEntity<RepairEstimationRequestDto> entity = new HttpEntity<>(request, authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-estimation/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("carModel: Car model is required", response.getBody().getMessage());
    }

    @Test
    void whenRequestBodyIsNull_thenReturnsBadRequest() {
	HttpHeaders headers = authHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);

	HttpEntity<String> entity = new HttpEntity<>(null, headers);

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-estimation/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("Request body is missing or invalid", response.getBody().getMessage());
    }

    @Test
    void whenInvalidJson_thenReturnsBadRequest() {
	HttpHeaders headers = authHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);

	HttpEntity<String> entity = new HttpEntity<>("{invalid json", headers);

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-estimation/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals("Request body is missing or invalid", response.getBody().getMessage());
    }

}
