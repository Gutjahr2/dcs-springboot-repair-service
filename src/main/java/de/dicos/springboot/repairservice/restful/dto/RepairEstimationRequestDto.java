package de.dicos.springboot.repairservice.restful.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import de.dicos.springboot.repairservice.restful.model.CarModel;
import de.dicos.springboot.repairservice.restful.model.RepairAction;
import io.swagger.v3.oas.annotations.media.Schema;

public class RepairEstimationRequestDto {

    @Schema(description = "Customerr car model", example = "AUDI_A3")
    @NotNull(message = "Car model is required")
    private CarModel carModel;

    @Schema(description = "Repair actions for customers car", example = "[\"OELWECHSEL\", \"BREMSBELAGWECHSEL\"]")
    @NotEmpty(message = "At least one repair action is required")
    private List<RepairAction> repairActions;

    public CarModel getCarModel() {
	return carModel;
    }

    public void setCarModel(CarModel carModel) {
	this.carModel = carModel;
    }

    public List<RepairAction> getRepairActions() {
	return repairActions;
    }

    public void setRepairActions(List<RepairAction> repairActions) {
	this.repairActions = repairActions;
    }
}
