/**
 * (c) DICOS GmbH, 2023
 *
 * $Id$
 */

package de.dicos.springboot.repairservice.restful.api;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dicos.springboot.repairservice.gen.model.RepairRequest;
import de.dicos.springboot.repairservice.restful.AdministrationService;
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

	// /////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////


	// /////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////

	@GetMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public void sendRepairRequest(@ParameterObject RepairRequest request)
		throws Exception
	{
		log.info("sending new repair request to administration");

		administrationService.postRepairRequest(request);
	}

	// /////////////////////////////////////////////////////////
	// Inner Classes
	// /////////////////////////////////////////////////////////


}
