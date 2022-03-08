package com.platform.recipe.model.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private Integer id;
    private String dishType;
    private Integer portionSize;
    private List<String> ingredients;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd‐MM‐yyyy HH:mm")
    private Timestamp createDate;
    private String cookingInstructions;
}
