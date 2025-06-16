package de.dicos.springboot.repairservice.restful.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CarModelValidatorTest {

    private CarModelValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
	validator = new CarModelValidator();
	context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void whenValidCarModel_thenIsValid() {
	assertTrue(validator.isValid("Audi A3", context));
	assertTrue(validator.isValid("BMW X5", context));
    }

    @Test
    void whenValidCarModelWithDifferentCase_thenIsValid() {
	assertTrue(validator.isValid("audi a3", context));
	assertTrue(validator.isValid("bmw x5", context));
    }

    @Test
    void whenValidCarModelWithSpaces_thenIsValid() {
	assertTrue(validator.isValid("  Audi A3  ", context));
    }

    @Test
    void whenNull_thenIsValid() {
	assertTrue(validator.isValid(null, context));
    }

    @Test
    void whenEmpty_thenIsValid() {
	assertTrue(validator.isValid("", context));
	assertTrue(validator.isValid("   ", context));
    }

    @Test
    void whenInvalidCarModel_thenIsInvalid() {
	assertFalse(validator.isValid("Ferrari F8", context));
	assertFalse(validator.isValid("Audi A9", context));
    }
}
