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

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController("/recipePlatform")
public class RecipeController {
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public RecipeService recipeService;

    @ApiOperation(value = "Add Recipe", nickname = "Add Recipe", notes = "This API accepts POST methods and can add new Recipe . If API execute successfully it returns 201 HTTP Code. It validates DishType ,Instructor and PortionSize. Recipe is saved on H2 in memory database")
    @PostMapping("/addRecipe")
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody @Valid RecipeRequest request) {
        RecipeDto recipeDto = modelMapper.map(request, RecipeDto.class);

        recipeDto = recipeService.addRecipe(recipeDto);
        RecipeResponse recipeResponse = modelMapper.map(recipeDto, RecipeResponse.class);



        ResponseEntity<RecipeResponse> response = new ResponseEntity<>(recipeResponse, HttpStatus.CREATED);
        return response;

    }

    @ApiOperation(value = "Delete Recipe", nickname = "Delete Recipe", notes = "This API accepts DELETE Method and it deletes recipe by id from database. If it execute successfully api return 200 HTTP code. If it doesn't find related recipe api throw an exception for controller advice")
    @DeleteMapping("/deleteRecipe")
    public ResponseEntity<Boolean> deleteRecipe(@RequestParam @NotBlank Integer recipeId) {

        Boolean deleteRecipe = recipeService.deleteRecipe(recipeId);
        ResponseEntity<Boolean> response = new ResponseEntity<>(deleteRecipe, HttpStatus.OK);
        return response;

    }

    @ApiOperation(value = "Update Recipe", nickname = "Update Recipe", notes = "This API accepts PUT methods and it updates related recipe . If it execute successfully it returns 200 HTTP Code.")
    @PutMapping("/updateRecipe")
    public ResponseEntity<Boolean> updateRecipe(@RequestBody @Valid  RecipeUpdateRequest recipeUpdateRequest) {
        RecipeDto recipeDto = modelMapper.map(recipeUpdateRequest, RecipeDto.class);
        Boolean updateRecipe = recipeService.updateRecipe(recipeDto);
        ResponseEntity<Boolean> response = new ResponseEntity<>(updateRecipe, HttpStatus.OK);
        return response;

    }


    @ApiOperation(value = "Get recipes", nickname = "Get recipes", notes = "This API accepts GET methods and it retrieve recipe list with dynamic queries. If it execute successfully api returns 200 HTTP code.")
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeResponse>> getRecipes(@RequestParam Optional<Object> id,@RequestParam Optional<Object> maxPortionSize, @RequestParam Optional<Object> minPortionSize, @RequestParam Optional<Object> dishType, @RequestParam Optional<Object> cookInstruction) {
        List<RecipeDto> recipeResponseDto = recipeService.searchRecipe(id,maxPortionSize, minPortionSize, dishType, cookInstruction);


        List<RecipeResponse> recipeList = recipeResponseDto
                .stream()
                .map(rec -> modelMapper.map(rec, RecipeResponse.class))
                .collect(Collectors.toList());


        ResponseEntity<List<RecipeResponse>> response = new ResponseEntity<List<RecipeResponse>>(recipeList, HttpStatus.OK);
        return response;
    }
}
