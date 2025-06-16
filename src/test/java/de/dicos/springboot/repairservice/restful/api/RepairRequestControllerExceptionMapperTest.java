package de.dicos.springboot.repairservice.restful.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairAppointmentResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairOperationDto;
import de.dicos.springboot.repairservice.restful.dto.RepairRequestDto;
import de.dicos.springboot.repairservice.restful.model.RepairAction;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 9999)
@ContextConfiguration(initializers = RepairRequestControllerExceptionMapperTest.PropertyOverrideInitializer.class)
public class RepairRequestControllerExceptionMapperTest {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
	return "http://localhost:" + port + contextPath + path;
    }

    @BeforeEach
    void setupMocks() {
	WireMock.reset();
    }

    public static class PropertyOverrideInitializer
	    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext context) {
	    TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, "administration.mock.enabled=false",
		    "administration.api.url=http://localhost:9999");
	}
    }

    private RepairRequestDto createValidRequestPayload() {
	RepairRequestDto request = new RepairRequestDto();
	request.setCustomerNumber(1234);
	request.setCarModel("Audi A3");
	request.setNumberPlate("RÜD-MG-6");
	request.setPreferredDate("01.09.2026");
	request.setRepairOperations(Collections.singletonList(new RepairOperationDto(RepairAction.OELWECHSEL, 405.84)));
	return request;
    }

    private HttpHeaders authHeaders() {
	HttpHeaders headers = new HttpHeaders();
	headers.setBasicAuth("testuser", "test");
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	return headers;
    }

    @Test
    void whenAdminSystemReturns400_thenMappedExceptionIsReturned() {
	stubFor(post(urlEqualTo("/repair-request")).willReturn(aResponse().withStatus(400).withBody("Bad request")));

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(createValidRequestPayload(), authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(400, response.getBody().getCode());
	assertEquals("Invalid request sent to the administration system.", response.getBody().getMessage());
    }

    @Test
    void whenAdminSystemReturnsOtherStatuscodeThanInApi_thenMappedExceptionIsReturned() {
	stubFor(post(urlEqualTo("/repair-request"))
		.willReturn(aResponse().withStatus(500).withBody("Internal server error")));

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(createValidRequestPayload(), authHeaders());

	ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url("/rest/repair-request/create"),
		entity, ErrorResponseDto.class);

	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(500, response.getBody().getCode());
	assertEquals("Unhandled HTTP status from administration system: 500", response.getBody().getMessage());
    }

    @Test
    void whenAdminSystemReturnsSuccess_thenRepairAppointmentIsReturned() {
	stubFor(post(urlEqualTo("/repair-request"))
		.willReturn(aResponse().withStatus(201).withHeader("Content-Type", "application/json").withBody("{}")));

	HttpEntity<RepairRequestDto> entity = new HttpEntity<>(createValidRequestPayload(), authHeaders());

	ResponseEntity<RepairAppointmentResponseDto> response = restTemplate
		.postForEntity(url("/rest/repair-request/create"), entity, RepairAppointmentResponseDto.class);

	assertEquals(HttpStatus.CREATED, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(createValidRequestPayload().getPreferredDate(), response.getBody().getRepairDate());
    }
}
