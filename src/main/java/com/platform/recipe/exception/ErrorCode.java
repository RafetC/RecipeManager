package com.platform.recipe.exception;


import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_RECIPE_ID_FOR_DELETION("ERR-002", "No entity for deletion!"),
    INVALID_RECIPE_DISH_TYPE("ERR-001", "Dish Type should be Vegeterian or Vegan or Meat!"),
    INVALID_RECIPE_ID_FOR_UPDATE("ERR-003", "No entity for update!");
    private String errCode;
    private String errMsgKey;

    private ErrorCode(final String errCode, final String errMsgKey) {
        this.errCode = errCode;
        this.errMsgKey = errMsgKey;
    }

}


