package com.platform.recipe.repository.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {
    private  String key;
    private String operation;
    private Optional<Object> value;
}
