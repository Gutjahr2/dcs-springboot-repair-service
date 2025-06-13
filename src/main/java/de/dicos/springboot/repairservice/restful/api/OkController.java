/**
 * (c) DICOS GmbH, 2020
 *
 * $Id$
 */

package de.dicos.springboot.repairservice.restful.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NoArgsConstructor;

/**
 *
 * @author bhochberger
 */
@RestController
@NoArgsConstructor
@RequestMapping(path = "/rest", //
				produces = MediaType.APPLICATION_JSON_VALUE)
public class OkController
{
	// /////////////////////////////////////////////////////////
	// Class Members
	// /////////////////////////////////////////////////////////


	// /////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////


	// /////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////

	/**
	 * @throws Exception
	 * 
	 */
	@GetMapping(value = "/ok", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> checkAvailability()
		throws Exception
	{
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	

	// /////////////////////////////////////////////////////////
	// Inner Classes
	// /////////////////////////////////////////////////////////


}
