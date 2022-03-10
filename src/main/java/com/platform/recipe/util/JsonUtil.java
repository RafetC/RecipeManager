package com.platform.recipe.util;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.platform.recipe.exception.ErrorCode;
import com.platform.recipe.exception.RecipePlatformRuntimeException;

import java.io.IOException;
import java.util.TimeZone;

public class JsonUtil {
    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false);
        mapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
        mapper.findAndRegisterModules();
        mapper.setTimeZone(TimeZone.getDefault());

    }

    public static String writeObject(Object value)   {
        try {
            return mapper.writeValueAsString(value);
        } catch (IOException ioException) {
            throw new RecipePlatformRuntimeException(ErrorCode.UNEXPECTED_ERROR);
        }
    }
}
