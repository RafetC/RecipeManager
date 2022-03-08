package com.platform.recipe.controller;

import com.platform.recipe.dto.RecipeDto;
import com.platform.recipe.model.contract.RecipeRequest;
import com.platform.recipe.model.contract.RecipeResponse;
import com.platform.recipe.model.contract.RecipeUpdateRequest;
import com.platform.recipe.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController("/recipePlatform")
public class RecipeController {
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public RecipeService recipeService;

    @ApiOperation(value = "Create Recipe", nickname = "Create Recipe", notes = "Create new recipe")
    @PostMapping("/addRecipe")
    public ResponseEntity<Integer> createRecipe(@RequestBody @Valid RecipeRequest request) {
        RecipeDto recipeDto = modelMapper.map(request, RecipeDto.class);

        Integer recipeId = recipeService.addRecipe(recipeDto);
        ResponseEntity<Integer> response = new ResponseEntity<>(recipeId, HttpStatus.CREATED);
        return response;

    }

    @ApiOperation(value = "Delete Recipe", nickname = "Delete Recipe", notes = "Delete  existing recipe")
    @DeleteMapping("/deleteRecipe")
    public ResponseEntity<Boolean> deleteRecipe(@RequestBody @NotBlank Integer recipeId) {

        Boolean deleteRecipe = recipeService.deleteRecipe(recipeId);
        ResponseEntity<Boolean> response = new ResponseEntity<>(deleteRecipe, HttpStatus.OK);
        return response;

    }

    @ApiOperation(value = "Update Recipe", nickname = "Update Recipe", notes = "Update  existing recipe")
    @PutMapping("/updateRecipe")
    public ResponseEntity<Boolean> updateRecipe(@RequestBody RecipeUpdateRequest recipeUpdateRequest) {
        RecipeDto recipeDto = modelMapper.map(recipeUpdateRequest, RecipeDto.class);
        Boolean updateRecipe = recipeService.updateRecipe(recipeDto);
        ResponseEntity<Boolean> response = new ResponseEntity<>(updateRecipe, HttpStatus.OK);
        return response;

    }


    @ApiOperation(value = "Get recipes", nickname = "Get recipes", notes = "Get recipes")
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeResponse>> getTrade(@RequestParam Optional<Object> maxPortionSize, @RequestParam Optional<Object> minPortionSize, @RequestParam Optional<Object> dishType, @RequestParam Optional<Object> cookInstruction) {
        List<RecipeDto> tradeResponseDto = recipeService.searchRecipe(maxPortionSize, minPortionSize, dishType, cookInstruction);


        List<RecipeResponse> recipeList = tradeResponseDto
                .stream()
                .map(rec -> modelMapper.map(rec, RecipeResponse.class))
                .collect(Collectors.toList());


        ResponseEntity<List<RecipeResponse>> response = new ResponseEntity<List<RecipeResponse>>(recipeList, HttpStatus.OK);
        return response;
    }
}
