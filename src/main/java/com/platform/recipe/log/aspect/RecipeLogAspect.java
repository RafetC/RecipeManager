package com.platform.recipe.log.aspect;


import com.platform.recipe.exception.ErrorCode;
import com.platform.recipe.exception.RecipePlatformRuntimeException;
import com.platform.recipe.log.RecipeLogWriter;
import com.platform.recipe.log.config.manager.LogConfigurationManager;
import com.platform.recipe.log.config.properties.LogConfigProperties;
import com.platform.recipe.log.config.properties.LogMode;
import com.platform.recipe.log.constants.LogConstants;
import com.platform.recipe.util.ContextUtil;
import com.platform.recipe.util.ExceptionUtil;
import com.platform.recipe.util.JsonUtil;
import com.platform.recipe.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Service
@ConditionalOnExpression("'${recipe.log.enable}'=='true'")
public class RecipeLogAspect {

    @Autowired
    private LogConfigProperties logConfigProperties;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY 'at' HH:mm:ss z");

    @Autowired(required = false)
    private RecipeLogWriter recipeLogWriter;

    @Around("execution(* com.platform.recipe.controller.RecipeController..*(..))")
    public Object prepareControllerLog(ProceedingJoinPoint proceedingJoinPoint) {
        return prepareLog(LogConstants.API_METHOD_LOG, proceedingJoinPoint);
    }

    private void prepareLogContent(String key, String value, LogMode logMode) {
        if (logMode == LogMode.KAFKA) {
            //TODO Kafka log implementation
        } else if (logMode == LogMode.FILE) {
            recipeLogWriter.appendLog("<" + key + ">" + value + "</" + key + ">");
        }

    }

    private Object prepareLog(String LogTag, ProceedingJoinPoint proceedingJoinPoint) {
        String requestedUrl = ContextUtil.getRequestedUrlInfoForLogging();

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();
        String methodType = GetCurrentMethodType(annotations);
        Object result = null;
        LogMode logMode = logConfigProperties.getLogMode();
        Object[] arguments = proceedingJoinPoint.getArgs().clone();
        if (checkLoggable(requestedUrl)) {
            recipeLogWriter.appendLog("<" + LogTag + ">");
            prepareLogContent(LogConstants.API_URL_TAG, requestedUrl, logMode);
            prepareLogContent(LogConstants.METHOD_NAME_TAG, methodSignature.getName(), logMode);
            prepareLogContent(LogConstants.METHOD_TYPE, methodType, logMode);
            prepareLogContent(LogConstants.THREAD_ID_TAG, String.valueOf(Thread.currentThread().getId()), logMode);
            prepareLogContent(LogConstants.START_TIME_TAG, formatter.format(ZonedDateTime.now()), logMode);
            prepareLogContent(LogConstants.PARAMS_TAG, JsonUtil.writeObject(arguments), logMode);


            try {
                result = proceedingJoinPoint.proceed();
                if (result != null)
                    prepareLogContent(LogConstants.OUTPUT_TAG, JsonUtil.writeObject(result), logMode);
                prepareLogContent(LogConstants.END_TIME_TAG, formatter.format(ZonedDateTime.now()), logMode);
            } catch (RecipePlatformRuntimeException recipeRuntimeException) {
                prepareException(recipeRuntimeException, methodSignature.getName(), logMode, requestedUrl, requestedUrl + UUID.randomUUID().toString(), arguments, LogTag);
                throw recipeRuntimeException;
            } catch (Throwable e) {
                prepareException(e, methodSignature.getName(), logMode, requestedUrl, requestedUrl + UUID.randomUUID().toString(), arguments, LogTag);
                throw new RecipePlatformRuntimeException(ErrorCode.UNEXPECTED_ERROR);
            }

            if (logMode == LogMode.KAFKA) {
                //TODO Kafka log implementation
            } else if (logMode == LogMode.FILE) {
                recipeLogWriter.appendLog("</" + LogTag + ">");
            }


        }
        return result;
    }

    private String GetCurrentMethodType(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(RequestMapping.class)) {
                RequestMapping[] reqMappingAnnotations = annotation.annotationType()
                        .getAnnotationsByType(RequestMapping.class);
                if (Arrays.stream(reqMappingAnnotations).count() > 1) {
                    throw new RecipePlatformRuntimeException(ErrorCode.ONE_MORE_REQUESTMAPPING);
                }
                return reqMappingAnnotations[0].method()[0].name();

            }
        }
        return null;
    }

    public void prepareException(Throwable e, String methodName, LogMode logMode, String url, String
            requestId, Object[] arguments, String methodType) {

        prepareLogContent(LogConstants.EXCEPTION_LOG_TAG, ExceptionUtil.getStackTrace(e), logMode);
        prepareLogContent(LogConstants.METHOD_NAME_TAG, methodName, logMode);
        prepareLogContent(LogConstants.METHOD_TYPE, methodType, logMode);
        if (!StringUtil.isNullOrEmpty(requestId))
            prepareLogContent(LogConstants.REQUEST_ID_TAG, requestId, logMode);
        if (!StringUtil.isNullOrEmpty(url))
            prepareLogContent(LogConstants.API_URL_TAG, url, logMode);
        if (!StringUtil.isNullOrEmpty(e.getMessage()))
            prepareLogContent(LogConstants.EXCEPTION_SUMMARY_LOG_TAG, e.getMessage(), logMode);
        if (arguments != null)
            prepareLogContent(LogConstants.PARAMS_TAG, JsonUtil.writeObject(arguments), logMode);


    }

    public boolean checkLoggable(String pathInfo) {
        return LogConfigurationManager.getInstance().checkLoggable(pathInfo);
    }


}
