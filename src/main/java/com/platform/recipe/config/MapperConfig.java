package com.platform.recipe.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper mp= new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mp;
    }
}

