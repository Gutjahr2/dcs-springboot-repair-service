package de.dicos.springboot.repairservice.restful.dto;

import javax.validation.constraints.NotNull;

import de.dicos.springboot.repairservice.restful.model.RepairAction;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto for the repair operations (description and price estimation)")
public class RepairOperationDto {

    @Schema(description = "Description of the repair operation", example = "Ölwechsel")
    @NotNull(message = "Repair operation description is required")
    private RepairAction description;

    @Schema(description = "Estimated price for the repair operation", example = "405.84")
    @NotNull(message = "Repair operation price estimation is required")
    private Double priceEstimation;

    public RepairOperationDto(RepairAction description, double priceEstimation) {
	this.description = description;
	this.priceEstimation = priceEstimation;
    }

    public RepairOperationDto() {
    }

    public RepairAction getDescription() {
	return description;
    }

    public void setDescription(RepairAction description) {
	this.description = description;
    }

    public Double getPriceEstimation() {
	return priceEstimation;
    }

    public void setPriceEstimation(Double priceEstimation) {
	this.priceEstimation = priceEstimation;
    }
}
