package com.platform.recipe.model.contract;

import com.platform.recipe.controller.validator.annotation.CheckDishType;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class RecipeRequest {
    @CheckDishType
    private String dishType;
    @Min(value = 1, message = "Portion size  should be 1 minimum")
    private Integer portionSize;
    private List<String> ingredients;
    @NotBlank(message = "Instruction shouldn't be blank")
    private String cookingInstructions;
}
