package de.dicos.springboot.repairservice.restful.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RepairOperationDto {

    @NotBlank(message = "Repair operation description is required")
    private String description;

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
