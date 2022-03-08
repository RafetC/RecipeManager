package com.platform.recipe.controller.validator;

import com.platform.recipe.controller.validator.annotation.CheckDishType;
import com.platform.recipe.exception.ErrorCode;
import com.platform.recipe.exception.RecipePlatformRuntimeException;
import com.platform.recipe.model.contract.RecipeRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DishTypeValidator implements ConstraintValidator<CheckDishType, RecipeRequest> {


    @Override
    public void initialize(CheckDishType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RecipeRequest recipeRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (recipeRequest != null) {
            if (recipeRequest.getDishType().equalsIgnoreCase("VEGAN") ||
                    recipeRequest.getDishType().equalsIgnoreCase("VEGETERIAN") ||
                    recipeRequest.getDishType().equalsIgnoreCase("MEAT")
            )
                return true;

        }
        throw new RecipePlatformRuntimeException(ErrorCode.INVALID_RECIPE_DISH_TYPE);
    }
}