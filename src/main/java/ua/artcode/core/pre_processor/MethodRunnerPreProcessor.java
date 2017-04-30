package ua.artcode.core.pre_processor;

/**
 * Created by v21k on 17.04.17.
 */

import java.io.IOException;
import java.net.URLClassLoader;

/**
 * This functional interface is using for getting paths for
 * class and it's root folders
 **/
@FunctionalInterface
public interface MethodRunnerPreProcessor {
    Class<?>[] getClasses(String[] classPaths, URLClassLoader classLoader)
            throws ClassNotFoundException, IOException;
}
