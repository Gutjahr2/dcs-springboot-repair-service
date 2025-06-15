package de.dicos.springboot.repairservice.restful.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class RepairOperationDto {

    @Schema(description = "Description of the repair operation", example= "Ölwechsel")
    @NotBlank(message = "Repair operation description is required")
    private String description;

    @Schema(description = "Estimated price for the repair operation", example= "405.84")
    @NotNull(message = "Repair operation price estimation is required")
    private Double priceEstimation;

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPriceEstimation() {
        return priceEstimation;
    }

    public void setPriceEstimation(Double priceEstimation) {
        this.priceEstimation = priceEstimation;
    }
}
