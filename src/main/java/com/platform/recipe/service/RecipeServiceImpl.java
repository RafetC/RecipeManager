package com.platform.recipe.service;

import com.platform.recipe.dto.RecipeDto;
import com.platform.recipe.exception.ErrorCode;
import com.platform.recipe.exception.RecipePlatformRuntimeException;
import com.platform.recipe.model.contract.RecipeResponse;
import com.platform.recipe.model.jpa.Ingredients;
import com.platform.recipe.model.jpa.Recipe;
import com.platform.recipe.repository.RecipeRepository;
import com.platform.recipe.repository.builder.RecipeDAO;
import com.platform.recipe.repository.builder.SearchCriteria;
import jdk.jfr.Timespan;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RecipeDAO recipeApi;
    @Autowired
    RecipeRepository recipeRepository;

    @Override
    public RecipeDto addRecipe(RecipeDto recipeDto) {

        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        mapRecipeDtoToRecipe(recipe, recipeDto);
        recipe.setCreateDate(getTimeSpampOfNow());
        recipeRepository.save(recipe);
        recipeDto.setId(recipe.getId());
        recipeDto.setCreateDate(recipe.getCreateDate());
        return recipeDto;

    }

    @Override
    public Boolean deleteRecipe(Integer receiptId) {
        recipeRepository.deleteById(receiptId);
        return true;
    }

    @Override
    public Boolean updateRecipe(RecipeDto updatedRecipe) {

        recipeRepository.findById(updatedRecipe.getId()).map(recipeForUpdate -> {
            recipeForUpdate.setCookingInstructions(updatedRecipe.getCookingInstructions());
            recipeForUpdate.setDishType(updatedRecipe.getDishType());
            recipeForUpdate.setPortionSize(updatedRecipe.getPortionSize());
            IntStream.range(0, recipeForUpdate.getIngredients().size())
                    .forEach(i -> recipeForUpdate.getIngredients().get(i).setName(updatedRecipe.getIngredients().get(i)));
            recipeForUpdate.setUpdateDate(getTimeSpampOfNow());
            recipeRepository.save(recipeForUpdate);
            return true;
        }).orElseThrow(() -> new RecipePlatformRuntimeException(ErrorCode.INVALID_RECIPE_ID_FOR_UPDATE));

        return true;
    }


    @Override
    public List<RecipeDto> searchRecipe(Optional<Object> Id, Optional<Object> maxPortionSize, Optional<Object> minPortionSize, Optional<Object> dishType, Optional<Object> cookInstruction) {
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriteria("portionSize", "<", maxPortionSize));
        searchCriteria.add(new SearchCriteria("portionSize", ">", minPortionSize));
        searchCriteria.add(new SearchCriteria("dishType", ":", dishType));
        searchCriteria.add(new SearchCriteria("cookingInstructions", "%", cookInstruction));
        searchCriteria.add(new SearchCriteria("id", ":", Id));

        List<Recipe> recipeList = recipeApi.searchRecipe(searchCriteria);


        List<RecipeDto> recipeDtoList = recipeList
                .stream()
                .map(rec -> modelMapper.map(rec, RecipeDto.class))
                .collect(Collectors.toList());

        for (int i = 0; i < recipeList.size(); i++) {
            recipeDtoList.get(i).setIngredients(recipeList.get(i).getIngredients().stream().map(p -> p.getName()).collect(Collectors.toList()));

        }
        return recipeDtoList;


    }

    private void mapRecipeDtoToRecipe(Recipe recipe, RecipeDto recipeDto) {
        List<Ingredients> ingredientsList = new ArrayList<>();
        if (recipeDto.getIngredients() != null) {
            for (int i = 0; i < recipeDto.getIngredients().size(); i++) {
                Ingredients ingredient = new Ingredients(null, null, recipeDto.getIngredients().get(i));
                ingredientsList.add(ingredient);

            }
            recipe.setIngredients(ingredientsList);
        }
    }

    public Timestamp getTimeSpampOfNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        Timestamp timestamp = Timestamp.valueOf(formatDateTime);
        return timestamp;
    }
}
