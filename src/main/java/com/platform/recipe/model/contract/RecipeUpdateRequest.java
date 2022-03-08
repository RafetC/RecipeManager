package com.platform.recipe.model.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.platform.recipe.controller.validator.annotation.CheckDishType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeUpdateRequest {
    @NotBlank
    private Integer id;
    private String dishType;
    private Integer portionSize;
    private List<String> ingredients;
    private String cookingInstructions;
}
