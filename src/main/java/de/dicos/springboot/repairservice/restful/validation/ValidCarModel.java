package de.dicos.springboot.repairservice.restful.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CarModelValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCarModel {
    String message() default "Invalid car model";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
