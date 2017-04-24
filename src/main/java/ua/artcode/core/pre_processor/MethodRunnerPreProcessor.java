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
    Class<?>[] getClasses(String[] classPaths) throws ClassNotFoundException, MalformedURLException;
}
