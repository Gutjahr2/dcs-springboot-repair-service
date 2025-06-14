package de.dicos.springboot.repairservice.restful.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RepairRequestDto {

    @NotNull(message = "Customer number is required")
    private Integer customerNumber;

    private String carModel;

    @NotBlank(message = "Number plate is required")
    private String numberPlate;

    private String preferredDate;

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
