package com.platform.recipe.model.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.platform.recipe.controller.validator.annotation.CheckDishType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class RecipeUpdateRequest {
    private Integer id;
    @CheckDishType
    private String dishType;
    @Min(value = 1, message = "Portion size  should be 1 minimum")
    private Integer portionSize;
    private List<String> ingredients;
    @NotBlank(message = "Instruction shouldn't be blank")
    private String cookingInstructions;
}
