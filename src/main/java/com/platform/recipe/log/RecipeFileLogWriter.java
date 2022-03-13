package com.platform.recipe.log;

import ch.qos.logback.classic.ClassicConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 import org.springframework.stereotype.Component;

@Component
 public class RecipeFileLogWriter implements RecipeLogWriter {

    private Logger logger= LoggerFactory.getLogger(RecipeFileLogWriter.class.getName());
    @Override
    public void appendLog(String logMessage) {
        logger.trace(logMessage);
    }

    @Override
    public void flushLog() {
        logger.trace(ClassicConstants.FINALIZE_SESSION_MARKER,"");
    }
}
