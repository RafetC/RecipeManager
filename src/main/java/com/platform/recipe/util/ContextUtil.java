package com.platform.recipe.util;


import com.platform.recipe.log.model.RequestContext;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.tomcat.util.buf.MessageBytes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ContextUtil {


    public static String getRequestedUrlInfoForLogging() {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        RequestContext requestContext = new RequestContext();
        if (requestAttributes != null) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            try {
                return ((RequestFacade) httpServletRequest).getRequestURI();
            } catch (ClassCastException ex) {

                return (httpServletRequest).getRequestURI();
            }
        }
        return null;
    }
}
