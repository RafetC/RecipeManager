package com.platform.recipe.service;

import com.platform.recipe.dto.RecipeDto;
import com.platform.recipe.model.contract.RecipeResponse;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    RecipeDto addRecipe(RecipeDto recipeDto);

    Boolean deleteRecipe(Integer receiptId);

    List<RecipeDto> searchRecipe(Optional<Object> id,Optional<Object> maxPortionSize, Optional<Object> minPortionSize, Optional<Object> dishType, Optional<Object> cookInstruction);

    Boolean updateRecipe(RecipeDto updatedRecipe);
}
