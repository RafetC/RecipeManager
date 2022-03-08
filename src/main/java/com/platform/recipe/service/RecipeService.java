package com.platform.recipe.service;

import com.platform.recipe.dto.RecipeDto;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Integer addRecipe(RecipeDto recipeDto);

    Boolean deleteRecipe(Integer receiptId);

    List<RecipeDto> searchRecipe(Optional<Object> maxPortionSize, Optional<Object> minPortionSize, Optional<Object> dishType, Optional<Object> cookInstruction);

    Boolean updateRecipe(RecipeDto updatedRecipe);
}
