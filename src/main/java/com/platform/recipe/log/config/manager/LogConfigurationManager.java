package com.platform.recipe.log.config.manager;

import com.platform.recipe.log.config.properties.LogConfigProperties;
import com.platform.recipe.util.BeanUtil;

public class LogConfigurationManager {

    private LogConfigProperties logConfigProperties;
    private static volatile LogConfigurationManager instance = null;

    public static synchronized LogConfigurationManager getInstance() {

        if (instance == null) {
            instance = new LogConfigurationManager();
        }
        return instance;
    }

    private LogConfigurationManager() {
        Configure();

    }

    private void Configure() {
        logConfigProperties = (LogConfigProperties) BeanUtil.getBean(LogConfigProperties.class);
    }


    public boolean checkLoggable(String requestPath) {
        if (logConfigProperties.getExclude() == null || logConfigProperties.getExclude().size() == 0) {
            return true;
        } else if (logConfigProperties.getExclude().stream().anyMatch(p -> requestPath.contains(p))) {
            return false;
        }
        return true;

    }
}
