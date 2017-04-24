package ua.artcode.core.method_runner;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import ua.artcode.model.response.MethodStats;

import java.lang.reflect.Method;

/**
 * Created by v21k on 16.04.17.
 */
public class Runners {
    public static MethodRunner main = (classes -> {
        for (Class<?> aClass : classes) {
            Method method = aClass.getMethod("main", String[].class);
            String[] argumentsForMain = null;
            // todo test this place with a negative test
            method.invoke(null, (Object) argumentsForMain);
        }
        return null;
    });

    // todo get JUnitCore.class and method run() and pass classes there

    public static MethodRunner test = (classes -> {
        Result result = JUnitCore.runClasses(classes);
        int overallTests = result.getRunCount();
        int failedTests = result.getFailureCount();
        int passedTests = overallTests - failedTests;
        return new MethodStats(overallTests, passedTests, failedTests, result.getFailures());
    });
}
