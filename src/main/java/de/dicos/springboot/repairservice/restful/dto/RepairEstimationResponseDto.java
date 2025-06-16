package de.dicos.springboot.repairservice.restful.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response with the estimated price")
public class RepairEstimationResponseDto {

    @Schema(description = "Estimated price for the repair")
    @JsonProperty("estimatedPrice")
    private double estimatedPrice;

    public RepairEstimationResponseDto() {
    }

    public RepairEstimationResponseDto(double estimatedPrice) {
	this.estimatedPrice = estimatedPrice;
    }

    public double getEstimatedPrice() {
	return estimatedPrice;
    }

    public void setEstimatedPrice(double estimatedPrice) {
	this.estimatedPrice = estimatedPrice;
    }
}
