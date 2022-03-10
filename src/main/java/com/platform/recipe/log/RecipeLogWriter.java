package com.platform.recipe.log;

public interface RecipeLogWriter {
    public void appendLog(String logMessage);
    public void flushLog();
}
