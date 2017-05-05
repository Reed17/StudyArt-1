package ua.artcode.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "profiling", havingValue = "true")
public class TimeExecutionProfiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeExecutionProfiler.class);

    private Map<String, Long> methodStats = new LinkedHashMap<>();

    private List<String> methodNamesOrdered = new ArrayList<>();
    private List<Integer> methodDepthValuesOrdered = new ArrayList<>();
    private StringBuffer message = new StringBuffer();

    private int depthLevel = 0;

    private String treePrefix = "___";

    /**
     * Profiling around endpoints - all classes annotated with @RestController annotation.
     *
     * @see TimeExecutionProfiler#reorderStats(List, List, Map)
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundEndPoints(ProceedingJoinPoint joinPoint) throws Throwable {
        message.append("\nPROFILING(execution depthLevel):\n")
                .append("Endpoint - ")
                .append(joinPoint.getSignature().getDeclaringType().getName())
                .append(".")
                .append(joinPoint.getSignature().getName());

        long endpointStart = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endpointEnd = System.currentTimeMillis();

        reorderStats(methodNamesOrdered, methodDepthValuesOrdered, methodStats)
                .forEach((key, value) ->
                        message.append("\n")
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

    /**
     * Profiling around core, service and utils classes.
     * To add more pointcuts - just add "|| [your_pointcut]" at the end.
     */
    @Around("execution(* ua.artcode.core.*.*(..))" +
            "|| execution(* ua.artcode.service.*.*(..))" +
            "|| execution(* ua.artcode.utils.*.*(..))" +
            "|| execution(* ua.artcode.utils.IO_utils.*.*(..))")
    public Object aroundServicesAndCore(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getDeclaringType().getName() + "." + joinPoint.getSignature().getName();
        methodNamesOrdered.add(methodName);

        methodDepthValuesOrdered.add(++depthLevel);

        long localStart = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long localEnd = System.currentTimeMillis();

        depthLevel--;

        long time = localEnd - localStart;
        methodStats.put(methodName, time);

        return result;
    }

    private void resetValues() {
        methodStats = new LinkedHashMap<>();
        methodNamesOrdered = new ArrayList<>();
        methodDepthValuesOrdered = new ArrayList<>();
        depthLevel = 0;
        message = new StringBuffer();
    }

    /**
     * Creating new map with ordered stats (according to methodNamesOrdered) with tree structure (prefixes)
     *
     * @param methodNamesOrdered       list of method names in execution order
     * @param methodDepthValuesOrdered same to methodNamesOrdered, but stores depthLevel level for every method
     * @param methodStats              map with stats for every method (methodName - execution time). Not ordered.
     * @return orderedStats Map with right method order and prefixes before method name (tree structure)
     */
    private Map<String, Long> reorderStats(List<String> methodNamesOrdered,
                                           List<Integer> methodDepthValuesOrdered,
                                           Map<String, Long> methodStats) {
        Map<String, Long> orderedStats = new LinkedHashMap<>();

        // generate new methodNamesOrdered list with tree structure
        List<String> newMethodOrder = getMethodWithTreeStructure(methodNamesOrdered, methodDepthValuesOrdered);

        for (int i = 0; i < methodNamesOrdered.size(); i++) {
            String methodNameWithPrefix = newMethodOrder.get(i);
            String cleanMethodName = methodNamesOrdered.get(i);

            orderedStats.put(methodNameWithPrefix, methodStats.get(cleanMethodName));
        }

        return orderedStats;
    }

    /**
     * Iterate through methodNamesOrdered list and add prefix
     * to every value according to value in methodDepthValuesOrdered
     *
     * @param methodNamesOrdered       list of method names in execution order
     * @param methodDepthValuesOrdered same to methodNamesOrdered, but stores depthLevel level for every method
     * @return new List (prefix + methodName)
     */
    private List<String> getMethodWithTreeStructure(List<String> methodNamesOrdered,
                                                    List<Integer> methodDepthValuesOrdered) {
        List<String> newMethodOrder = new ArrayList<>();

        // iterate through methodNameOrdered
        for (int i = 0; i < methodNamesOrdered.size(); i++) {

            // generate prefix for method name
            StringBuffer prefix = new StringBuffer();
            for (int j = 0; j < methodDepthValuesOrdered.get(i); j++) {
                prefix.append(treePrefix);
            }

            // add prefix + methodName to new list
            newMethodOrder.add("|" + prefix + methodNamesOrdered.get(i));
        }
        return newMethodOrder;
    }
}
