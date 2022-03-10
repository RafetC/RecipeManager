package com.platform.recipe.log.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RequestContext implements Serializable {

    private String requestUrl;
    private String pathInfo;
    private String requestId;

}
