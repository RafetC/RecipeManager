package com.platform.recipe;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.recipe.model.contract.RecipeRequest;
import com.platform.recipe.model.contract.RecipeResponse;

import com.platform.recipe.model.contract.RecipeUpdateRequest;
import com.platform.recipe.repository.RecipeRepository;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class RecipeApplicationTests {

    @Test
    public void contextLoads() {
    }

    ObjectMapper om = new ObjectMapper();
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    MockMvc mockMvc;

    Map<String, RecipeRequest> testData;
    Map<String, RecipeResponse> testDataresponse;

    @Before
    public void setup() {
        recipeRepository.deleteAll();
        testData = getTestDataRequest();
        testDataresponse = getTestDataResponse();
    }

    @Test
    public void GivenCreateRecipe_whenNullIngredientsandVeganType_thenReturnRecipe() throws Exception {
        //test type Vegan
        RecipeResponse expectedRecord = testDataresponse.get("recipe_data1");
        RecipeRequest requestRecord = testData.get("recipe_data1");

        assertEquals("Vegan", expectedRecord.getDishType());

        RecipeResponse actualRecord = om.readValue(mockMvc.perform(post("/addRecipe")
                        .contentType("application/json")
                        .content(om.writeValueAsString(requestRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord, new String[]{"id", "createDate"}).matches(actualRecord));
        assertEquals(true, recipeRepository.findById(actualRecord.getId()).isPresent());
    }

    @Test
    public void GivenCreateRecipe_whenNullIngredientsandMeat_thenReturnRecipe() throws Exception {
        //test type Meat
        RecipeResponse expectedRecord = testDataresponse.get("recipe_data1");
        expectedRecord.setDishType("MEAT");
        RecipeRequest requestRecord = testData.get("recipe_data1");
        requestRecord.setDishType("MEAT");
        assertEquals("MEAT", expectedRecord.getDishType());

        RecipeResponse actualRecord = om.readValue(mockMvc.perform(post("/addRecipe")
                        .contentType("application/json")
                        .content(om.writeValueAsString(requestRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord, new String[]{"id", "createDate"}).matches(actualRecord));
        assertEquals(true, recipeRepository.findById(actualRecord.getId()).isPresent());
    }

    @Test
    public void GivenCreateRecipe_whenNotNullIngredientsandMeat_thenReturnRecipe() throws Exception {
        //test type Meat
        RecipeResponse expectedRecord = testDataresponse.get("recipe_data2");
        expectedRecord.setDishType("MEAT");
        RecipeRequest requestRecord = testData.get("recipe_data2");
        requestRecord.setDishType("MEAT");
        assertEquals("MEAT", expectedRecord.getDishType());

        RecipeResponse actualRecord = om.readValue(mockMvc.perform(post("/addRecipe")
                        .contentType("application/json")
                        .content(om.writeValueAsString(requestRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord, new String[]{"id", "createDate"}).matches(actualRecord));
        assertEquals(true, recipeRepository.findById(actualRecord.getId()).isPresent());
    }


    @Test
    public void GivenCreateRecipe_whenNullIngredientsandInvalidDishType_thenReturnBadRequest() throws Exception {
        //test invalid Dishtype

        RecipeRequest requestRecord = testData.get("recipe_data1");
        requestRecord.setDishType("DRINK");


        mockMvc.perform(post("/addRecipe")
                        .contentType("application/json")
                        .content(om.writeValueAsString(requestRecord)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void GivenCreateRecipe_whenNullIngredientsandInvalidPortion_thenReturnBadRequest() throws Exception {
        //test invalid Portion

        RecipeRequest requestRecord = testData.get("recipe_data1");
        requestRecord.setPortionSize(0);


        mockMvc.perform(post("/addRecipe")
                        .contentType("application/json")
                        .content(om.writeValueAsString(requestRecord)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void GivenCreateRecipe_whenNullIngredientsandNullInstruction_thenReturnBadRequest() throws Exception {
        //test empty instruction

        RecipeRequest requestRecord = testData.get("recipe_data1");
        requestRecord.setCookingInstructions(null);


        mockMvc.perform(post("/addRecipe")
                        .contentType("application/json")
                        .content(om.writeValueAsString(requestRecord)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void GivenGetRecipe_whenWithoutQuery_thenReturnRecipeList() throws Exception {
        Map<String, RecipeRequest> testData = getTestDataRequest();

        Map<String, RecipeResponse> expectedMap = new HashMap<>();
        List<RecipeResponse> expected = new ArrayList<>();
        for (Map.Entry<String, RecipeRequest> kv : testData.entrySet()) {
            RecipeResponse response = om.readValue(mockMvc.perform(post("/addRecipe")
                            .contentType("application/json")
                            .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);
            expectedMap.put(kv.getKey(), response);
        }
        Collections.sort(Arrays.asList(expectedMap.values().toArray(new RecipeResponse[testData.size()])), Comparator.comparing(RecipeResponse::getId));

        //without filter
        List<RecipeResponse> actualRecords = om.readValue(mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<RecipeResponse>>() {
        });
        expected = expectedMap.values().stream().collect(Collectors.toList());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expected.get(i), new String[]{"id", "createDate", "ingredients"}).matches(actualRecords.get(i)));
        }
    }


    @Test
    public void GivenGetRecipe_whenWithPortionLimit_thenReturnRecipeList() throws Exception {
        Map<String, RecipeRequest> testData = getTestDataRequest();

        Map<String, RecipeResponse> expectedMap = new HashMap<>();
        List<RecipeResponse> expected = new ArrayList<>();
        for (Map.Entry<String, RecipeRequest> kv : testData.entrySet()) {
            RecipeResponse response = om.readValue(mockMvc.perform(post("/addRecipe")
                            .contentType("application/json")
                            .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);
            expectedMap.put(kv.getKey(), response);
        }
        Collections.sort(Arrays.asList(expectedMap.values().toArray(new RecipeResponse[testData.size()])), Comparator.comparing(RecipeResponse::getId));

        //with portion filter  ?maxPortionSize=15&minPortionSize=10
        List<RecipeResponse> actualRecords = om.readValue(mockMvc.perform(get("/recipes?maxPortionSize=15&minPortionSize=9"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<RecipeResponse>>() {
        });
        expected = expectedMap.values().stream().filter(p -> p.getPortionSize() > 9 && p.getPortionSize() < 15).collect(Collectors.toList());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expected.get(i), new String[]{"id", "createDate"}).matches(actualRecords.get(i)));
        }


    }

    @Test
    public void GivenGetRecipe_whenNWithInstructionSearch_thenReturnRecipeList() throws Exception {
        Map<String, RecipeRequest> testData = getTestDataRequest();

        Map<String, RecipeResponse> expectedMap = new HashMap<>();
        List<RecipeResponse> expected = new ArrayList<>();
        for (Map.Entry<String, RecipeRequest> kv : testData.entrySet()) {
            RecipeResponse response = om.readValue(mockMvc.perform(post("/addRecipe")
                            .contentType("application/json")
                            .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);
            expectedMap.put(kv.getKey(), response);
        }
        Collections.sort(Arrays.asList(expectedMap.values().toArray(new RecipeResponse[testData.size()])), Comparator.comparing(RecipeResponse::getId));

        //with instruction filter  cookInstruction=sugar
        List<RecipeResponse> actualRecords = om.readValue(mockMvc.perform(get("/recipes?cookInstruction=sugar"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<RecipeResponse>>() {
        });
        expected = expectedMap.values().stream().filter(p -> p.getCookingInstructions().contains("sugar")).collect(Collectors.toList());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expected.get(i), new String[]{"id", "createDate"}).matches(actualRecords.get(i)));
        }


    }

    @Test
    public void GivenGetRecipe_whenWithId_thenReturnRecipe() throws Exception {
        Map<String, RecipeRequest> testData = getTestDataRequest();

        Map<String, RecipeResponse> expectedMap = new HashMap<>();
        List<RecipeResponse> expected = new ArrayList<>();
        for (Map.Entry<String, RecipeRequest> kv : testData.entrySet()) {
            RecipeResponse response = om.readValue(mockMvc.perform(post("/addRecipe")
                            .contentType("application/json")
                            .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);
            expectedMap.put(kv.getKey(), response);
        }
        Collections.sort(Arrays.asList(expectedMap.values().toArray(new RecipeResponse[testData.size()])), Comparator.comparing(RecipeResponse::getId));

        //with portion filter  ?maxPortionSize=15&minPortionSize=10
        List<RecipeResponse> actualRecords = om.readValue(mockMvc.perform(get("/recipes?id=3"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<RecipeResponse>>() {
        });
        expected = expectedMap.values().stream().filter(p -> p.getId() == 3).collect(Collectors.toList());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertTrue(new ReflectionEquals(expected.get(i), new String[]{"id", "createDate"}).matches(actualRecords.get(i)));
        }


    }


    @Test
    public void GivenGetRecipe_whenWithdeletebyId_thenReturnRecipe() throws Exception {
        Map<String, RecipeRequest> testData = getTestDataRequest();

        Map<String, RecipeResponse> expectedMap = new HashMap<>();
        List<RecipeResponse> expected = new ArrayList<>();
        for (Map.Entry<String, RecipeRequest> kv : testData.entrySet()) {
            RecipeResponse response = om.readValue(mockMvc.perform(post("/addRecipe")
                            .contentType("application/json")
                            .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);
            expectedMap.put(kv.getKey(), response);
        }
        Collections.sort(Arrays.asList(expectedMap.values().toArray(new RecipeResponse[testData.size()])), Comparator.comparing(RecipeResponse::getId));

        //delete by id
        Boolean isDeleted = om.readValue(mockMvc.perform(delete("/deleteRecipe")
                        .contentType("application/json")
                        .content("3"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Boolean.class);
        Assert.assertTrue(isDeleted);
    }

    @Test
    public void GivenGetRecipe_whenWithUpdate_thenReturnRecipe() throws Exception {
        Map<String, RecipeRequest> testData = getTestDataRequest();

        Map<String, RecipeResponse> expectedMap = new HashMap<>();
        List<RecipeResponse> expected = new ArrayList<>();
        for (Map.Entry<String, RecipeRequest> kv : testData.entrySet()) {
            RecipeResponse response = om.readValue(mockMvc.perform(post("/addRecipe")
                            .contentType("application/json")
                            .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RecipeResponse.class);
            expectedMap.put(kv.getKey(), response);
        }
        Collections.sort(Arrays.asList(expectedMap.values().toArray(new RecipeResponse[testData.size()])), Comparator.comparing(RecipeResponse::getId));

        Boolean isUpdated = om.readValue(mockMvc.perform(put("/updateRecipe")
                        .contentType("application/json")
                        .content(om.writeValueAsString(getUpdateRequest())))
                .andDo(print())

                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Boolean.class);

        Assert.assertTrue(isUpdated);
    }


    public RecipeUpdateRequest getUpdateRequest() {
        return new RecipeUpdateRequest(3, "MEAT", 50, getSampleIngredientList(), "Updated");

    }


    private Map<String, RecipeRequest> getTestDataRequest() {
        Map<String, RecipeRequest> data = new HashMap<>();

        RecipeRequest recipe_data1 = new RecipeRequest(

                "Vegan",
                3,
                null,

                "Two eggs add, prepare pizza and cook 15 minutes"

        );
        data.put("recipe_data1", recipe_data1);

        RecipeRequest recipe_data2 = new RecipeRequest(

                "Meat",
                10,
                getSampleIngredientList(),

                "Two eggs add, prepare chicken , add salt"

        );
        data.put("recipe_data2", recipe_data2);


        RecipeRequest recipe_data3 = new RecipeRequest(

                "Vegeterian",
                3,
                getSampleIngredientList1(),

                "Two eggs add, prepare sugar , add water"

        );
        data.put("recipe_data3", recipe_data3);

        RecipeRequest recipe_data4 = new RecipeRequest(

                "Vegeterian",
                12,
                getSampleIngredientList2(),

                "Two eggs add, prepare spove , add water"

        );
        data.put("recipe_data4", recipe_data4);
        return data;
    }


    private Map<String, RecipeResponse> getTestDataResponse() {
        Map<String, RecipeResponse> data = new HashMap<>();

        RecipeResponse recipe_data1 = new RecipeResponse(
                1,
                "Vegan",
                3,
                null,
                null,

                "Two eggs add, prepare pizza and cook 15 minutes"

        );
        data.put("recipe_data1", recipe_data1);

        RecipeResponse recipe_data2 = new RecipeResponse(
                1,
                "Meat",
                10,
                getSampleIngredientList(),
                null,
                "Two eggs add, prepare chicken , add salt"

        );
        data.put("recipe_data2", recipe_data2);


        RecipeResponse recipe_data3 = new RecipeResponse(
                3,
                "Vegetable",
                3,
                getSampleIngredientList1(),
                null,
                "Two eggs add, prepare sugar , add water"

        );
        data.put("recipe_data3", recipe_data3);

        RecipeResponse recipe_data4 = new RecipeResponse(
                1,
                "Vegetable",
                12,
                getSampleIngredientList2(),
                null,
                "Two eggs add, prepare spove , add water"

        );
        data.put("recipe_data4", recipe_data4);
        return data;
    }


    public List<String> getSampleIngredientList() {
        List<String> ingredientsList = new ArrayList<>();

        ingredientsList.add("2 Eggs");

        ingredientsList.add("Olive oil");

        ingredientsList.add("Butter");

        ingredientsList.add("Chicken");

        return ingredientsList;
    }

    public List<String> getSampleIngredientList1() {
        List<String> ingredientsList = new ArrayList<>();


        ingredientsList.add("Sugar");


        ingredientsList.add("Salt");

        ingredientsList.add("Egg");

        ingredientsList.add("Rice");


        return ingredientsList;
    }

    public List<String> getSampleIngredientList2() {
        List<String> ingredientsList = new ArrayList<>();

        ingredientsList.add("Water");

        ingredientsList.add("Minerals");


        ingredientsList.add("Vitamins");

        ingredientsList.add("Curry Leaf");


        return ingredientsList;
    }

}
