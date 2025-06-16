package de.dicos.springboot.repairservice.restful.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import de.dicos.springboot.repairservice.restful.validation.ValidCarModel;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "RequestDto for the repair request")
public class RepairRequestDto {

    @Schema(description = "Date for repair", example = "1234")
    @NotNull(message = "Customer number is required")
    private Integer customerNumber;

    @Schema(description = "Customer car model", example = "Audi A3")
    @ValidCarModel
    private String carModel;

    @Schema(description = "Customer number Plate", example = "RÜD-MG-6")
    @NotBlank(message = "Number plate is required")
    private String numberPlate;

    @Schema(description = "Date for repair", example = "01.09.2026")
    private String preferredDate;

    @Schema(description = "List of repair operations", example = "[{\"description\": \"Ölwechsel\", \"priceEstimation\": 405.84}]")
    @NotEmpty(message = "At least one repair operation is required")
    @Valid
    private List<RepairOperationDto> repairOperations;

    // Getters and Setters

    public Integer getCustomerNumber() {
	return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
	this.customerNumber = customerNumber;
    }

    public String getCarModel() {
	return carModel;
    }

    public void setCarModel(String carModel) {
	this.carModel = carModel;
    }

    public String getNumberPlate() {
	return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
	this.numberPlate = numberPlate;
    }

    public String getPreferredDate() {
	return preferredDate;
    }

    public void setPreferredDate(String preferredDate) {
	this.preferredDate = preferredDate;
    }

    public List<RepairOperationDto> getRepairOperations() {
	return repairOperations;
    }

    public void setRepairOperations(List<RepairOperationDto> repairOperations) {
	this.repairOperations = repairOperations;
    }
}
