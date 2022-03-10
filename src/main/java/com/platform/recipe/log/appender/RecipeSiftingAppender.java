package com.platform.recipe.log.appender;


import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class RecipeSiftingAppender extends SiftingAppender {

    @Override
    protected void append(ILoggingEvent event)
    {
        super.append(event);
        if(eventMarksEndOfLife(event)){
            long timestamp=this.getTimestamp(event);
            this.appenderTracker.endOfLife(event.getMDCPropertyMap().get("fileLocation"));
            this.appenderTracker.removeStaleComponents(timestamp);
            this.appenderTracker.find(event.getMDCPropertyMap().get("fileLocation")).stop();
            this.appenderTracker.setTimeout(1000);
        }
    }


}
