package ua.artcode.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by v21k on 04.05.17.
 */
@Aspect
@Component
public class TimeExecutionProfiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeExecutionProfiler.class);

    private Map<String, Long> stats = new LinkedHashMap<>();
    private List<String> methodOrder = new ArrayList<>();
    private List<Integer> methodStack = new ArrayList<>();
    private StringBuffer message = new StringBuffer();

    private int order = 0;

    // todo tree structure

    /**
     * Profiling around endpoints - all classes annotated with @RestController annotatio*
     *
     * @see TimeExecutionProfiler#reorderStats()
     */
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
        methodOrder.add(methodName);


        methodStack.add(++order);
        long localStart = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long localEnd = System.currentTimeMillis();
        order--;

        long time = localEnd - localStart;
        stats.put(methodName, time);

        return result;
    }

    private void resetValues() {
        stats = new LinkedHashMap<>();
        methodOrder = new ArrayList<>();
        methodStack = new ArrayList<>();
        order = 0;
        message = new StringBuffer();
    }

    /**
     * Reordering stats to match invocation order
     */
    private Map<String, Long> reorderStats() {
        Map<String, Long> orderedStats = new LinkedHashMap<>();

        // generate new methodOrder list with tree structure
        List<String> newMethodOrder = getMethodWithTreeStructure();

        for (int i = 0; i < methodOrder.size(); i++) {
            String methodNameWithPrefix = newMethodOrder.get(i);
            String cleanMethodName = methodOrder.get(i);

            orderedStats.put(methodNameWithPrefix, stats.get(cleanMethodName));
        }

        return orderedStats;
    }

    private List<String> getMethodWithTreeStructure() {
        List<String> newMethodOrder = new ArrayList<>();
        for (int i = 0; i < methodOrder.size(); i++) {
            StringBuilder prefix = new StringBuilder();
            for (int j = 0; j < methodStack.get(i); j++) {
                prefix.append("_");
            }
            newMethodOrder.add("|" + prefix + methodOrder.get(i));
        }
        return newMethodOrder;
    }
}
