package de.dicos.springboot.repairservice.restful.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Confirmation of the appointment with date")
public class RepairAppointmentResponseDto {

    @Schema(description = "Date for repair")
    @JsonProperty("repairDate")
    private String repairDate;

    public RepairAppointmentResponseDto(String repairDate) {
	this.repairDate = repairDate;
    }
    
    public RepairAppointmentResponseDto() {
	
    }
    
    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }
    
}