package com.platform.recipe.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Recipe")
public class Recipe {

    @SequenceGenerator(
            name = "recipe_id_seq",
            sequenceName = "recipe_id_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "recipe_id_seq"
    )
    @Id

    @Column(name = "id")
    private Integer id;
    private String dishType;
    private Integer portionSize;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "Recipe_Id")
    private List<Ingredients> ingredients = new ArrayList<>();

    private Timestamp createDate;
    private Timestamp updateDate;
    private String cookingInstructions;
}
