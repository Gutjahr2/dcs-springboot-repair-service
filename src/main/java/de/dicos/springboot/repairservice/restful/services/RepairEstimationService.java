package de.dicos.springboot.repairservice.restful.services;

import javax.validation.Valid;

import de.dicos.springboot.repairservice.restful.dto.RepairEstimationRequestDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationResponseDto;

public class RepairEstimationService {

    public RepairEstimationResponseDto estimateCosts(@Valid RepairEstimationRequestDto request) {
	return null;
    }

}
