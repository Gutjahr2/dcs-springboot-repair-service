package de.dicos.springboot.repairservice.restful.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Confirmation of the appointment with date")
public class RepairAppointmentResponseDto {

    @Schema(description = "Date for repair", example = "01.09.2026")
    private String repairDate;

    public RepairAppointmentResponseDto() {}
    
    public RepairAppointmentResponseDto(String repairDate) {
	this.repairDate = repairDate;
    }
    
    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }
    
}