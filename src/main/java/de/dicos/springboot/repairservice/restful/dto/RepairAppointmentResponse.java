package de.dicos.springboot.repairservice.restful.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Confirmation of the appointment with date")
public class RepairAppointmentResponse {

    @Schema(description = "Date for repair")
     private String date;

    public RepairAppointmentResponse(String date) {
	this.date = date;
    }
    
}