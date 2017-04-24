package ua.artcode.core.pre_processor;

/**
 * Created by v21k on 17.04.17.
 */

import java.net.MalformedURLException;

/**
 * This functional interface is using for getting paths for
 * class and it's root folders
 **/
public interface MethodRunnerPreProcessor {
    /**
     * @return String[] - 1st element - path for runnable class, 2nd - root folder for project
     */
    Class<?>[] getClasses(String[] classPaths, String methodName) throws ClassNotFoundException, MalformedURLException;
}
