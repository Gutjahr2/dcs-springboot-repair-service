package de.dicos.springboot.repairservice.restful.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.dicos.springboot.repairservice.restful.model.CarModel;

public class CarModelValidator implements ConstraintValidator<ValidCarModel, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
	if (value == null || value.trim().isEmpty()) {
	    return true;
	}

	return Arrays.stream(CarModel.values()).anyMatch(e -> e.getCsvLabel().equalsIgnoreCase(value.trim()));
    }
}
