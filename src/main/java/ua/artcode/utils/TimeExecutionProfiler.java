package ua.artcode.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v21k on 04.05.17.
 */
@Aspect
@Component
public class TimeExecutionProfiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeExecutionProfiler.class);

    private Map<String, Long> stats = new LinkedHashMap<>();
    private List<String> methodOrder = new ArrayList<>();

    private StringBuffer message = new StringBuffer();


    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundEndPoints(ProceedingJoinPoint joinPoint) throws Throwable {
        message.append("\nPROFILING(execution order):\n")
                .append("Endpoint - ")
                .append(joinPoint.getSignature().getDeclaringType().getName())
                .append(".")
                .append(joinPoint.getSignature().getName());

        long endpointStart = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endpointEnd = System.currentTimeMillis();

        reorderStats().forEach((key, value) ->
                message.append("\nMethod name - ")
                        .append(key)
                        .append(". Execution time - ")
                        .append(value));

        message.append("\nDONE. ")
                .append("Overall time - ")
                .append(endpointEnd - endpointStart);

        LOGGER.info(message.toString());
        resetValues();
        return result;
    }

    @Around("execution(* ua.artcode.core.*.*(..))" +
            "|| execution(* ua.artcode.service.*.*(..))" +
            "|| execution(* ua.artcode.utils.*.*(..))" +
            "|| execution(* ua.artcode.utils.IO_utils.*.*(..))")
    public Object aroundServicesAndCore(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getDeclaringType().getName() + "." + joinPoint.getSignature().getName();
        methodOrder.add(methodName);
        long localStart = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long localEnd = System.currentTimeMillis();

        long time = localEnd - localStart;
        stats.put(methodName, time);

        return result;
    }

    private void resetValues() {
        stats = new LinkedHashMap<>();
        methodOrder = new ArrayList<>();
        message = new StringBuffer();
    }

    private Map<String, Long> reorderStats() {
        Map<String, Long> orderedStats = new LinkedHashMap<>();
        methodOrder.forEach(methodName -> orderedStats.put(methodName, stats.get(methodName)));
        return orderedStats;
    }
}
