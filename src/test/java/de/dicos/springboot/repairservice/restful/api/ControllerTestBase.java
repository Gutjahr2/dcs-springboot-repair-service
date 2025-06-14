/**
 * (c) DICOS GmbH, 2020
 *
 * $Id$
 */

package de.dicos.springboot.repairservice.restful.api;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResponseErrorHandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import de.dicos.springboot.repairservice.restful.exception.AdministrationSystemResponseExceptionMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author sth
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@NoArgsConstructor
@Slf4j
public class ControllerTestBase
{
	// /////////////////////////////////////////////////////////
	// Class Members
	// /////////////////////////////////////////////////////////

    	private static final Logger log = LoggerFactory.getLogger(ControllerTestBase.class);
    
	@Value("${server.port}")
	protected int port;

	@Autowired
	protected TestRestTemplate restTemplate;

	protected String restBase;

	protected HttpStatus expectedStatus = null;

	// /////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////


	// /////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////

	public void cleanupBeforeTest(String... permissions)
	{
		restBase = "http://localhost:" + port + "/repair-service/rest/";

		restTemplate.getRestTemplate().setErrorHandler(new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse response)
				throws IOException
			{
				return !response.getStatusCode().is2xxSuccessful();
			}

			@Override
			public void handleError(ClientHttpResponse response)
				throws IOException
			{
				if (response.getStatusCode() == expectedStatus) {
					log.info("Got expected non-success status " + expectedStatus);
					throw new ExpectedStatusResponse(response);
				} else {
					log.error("got error status in response: " + response.getStatusCode());
					throw new IllegalStateException("got error status in response: " + response.getStatusCode());
				}
			}
		});

		restTemplate.getRestTemplate().setInterceptors(Arrays.asList(new LoggingInterceptor()));

		expectedStatus = null;
	}

	/**
	 * @param string
	 * @param debug
	 */
	protected static void setLogLevel(Class<?> cls, Level level)
	{
		setLogLevel(cls.getName(), level);
	}

	/**
	 * @param string
	 * @param debug
	 */
	protected static void setLogLevel(String name, Level level)
	{
		LoggerContext c = (LoggerContext) LoggerFactory.getILoggerFactory();
		c.getLogger(name).setLevel(level);
	}

	// /////////////////////////////////////////////////////////
	// Inner Classes
	// /////////////////////////////////////////////////////////

	private class LoggingInterceptor
		implements
		ClientHttpRequestInterceptor
	{
		@Override
		public ClientHttpResponse intercept(HttpRequest req, byte[] reqBody, ClientHttpRequestExecution ex)
			throws IOException
		{
			log.info("-------------------------------------------------------");
			log.info(">> Request: {} {}", req.getMethodValue(), req.getURI());
			log.info(">> Request headers: {}", req.getHeaders());

			ClientHttpResponse response = ex.execute(req, reqBody);

			log.info("<< Response status: {} {}", response.getRawStatusCode(), response.getStatusText());
			log.info("<< Response headers: {}", response.getHeaders());
			log.info("-------------------------------------------------------");
			return response;
		}
	}
}
