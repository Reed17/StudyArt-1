package ua.artcode.core.pre_processor;

/**
 * Created by v21k on 17.04.17.
 */

/**
 * This functional interface is using for getting paths for
 * class and it's root folders
 **/
public interface MethodRunnerPreProcessor {
    /**
     * @return String[] - 1st element - path for runnable class, 2nd - root folder for project
     */
    String[] getPaths(String[] classPaths) throws ClassNotFoundException;
}
