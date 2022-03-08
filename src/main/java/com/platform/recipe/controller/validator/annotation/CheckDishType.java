package com.platform.recipe.controller.validator.annotation;

 import com.platform.recipe.controller.validator.DishTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DishTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckDishType {

    String message() default "Dish Type should be Vegeterian or Vegan or Meat";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
