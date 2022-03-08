package com.platform.recipe.controller.validator;

import com.platform.recipe.controller.validator.annotation.CheckDishType;
import com.platform.recipe.exception.ErrorCode;
import com.platform.recipe.exception.RecipePlatformRuntimeException;
import com.platform.recipe.model.contract.RecipeRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DishTypeValidator implements ConstraintValidator<CheckDishType, String> {


    @Override
    public void initialize(CheckDishType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String stringValue, ConstraintValidatorContext constraintValidatorContext) {
        if (stringValue != null) {
            if (stringValue.equalsIgnoreCase("VEGAN") ||
                    stringValue.equalsIgnoreCase("VEGETERIAN") ||
                    stringValue.equalsIgnoreCase("MEAT")
            )
                return true;

        }
        throw new RecipePlatformRuntimeException(ErrorCode.INVALID_RECIPE_DISH_TYPE);
    }
}