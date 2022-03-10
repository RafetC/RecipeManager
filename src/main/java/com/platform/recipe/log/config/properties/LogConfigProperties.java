package com.platform.recipe.log.config.properties;


 import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration

@PropertySource("classpath:log/log.properties")
@ConfigurationProperties(prefix = "recipe.log")
public class LogConfigProperties {

    private boolean enable;
    private LogMode logMode;
    private LogLevel logLevel;
    private String logPath;
    private List<String> exclude;

    public LogLevel getLogLevel() {
        if (this.logLevel == null)
            return LogLevel.INFO;
        else
            return this.logLevel;
    }

    public List<String> getExclude() {
        if (exclude == null || exclude.size() == 0) {
            exclude = new ArrayList<>();
        }
        if (!exclude.contains("/swagger-ui.html")) {
            exclude.add("/swagger-ui.html");
        }
        return exclude;
    }


}
