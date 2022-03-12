package com.platform.recipe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {

    private Integer id;
    private String dishType;
    private Integer portionSize;
    private String dishName;

    private List<String> ingredients;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd‐MM‐yyyy HH:mm")
    private Timestamp createDate;
    private String cookingInstructions;
}


