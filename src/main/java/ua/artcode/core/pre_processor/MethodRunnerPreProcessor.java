package ua.artcode.core.pre_processor;

/**
 * Created by v21k on 17.04.17.
 */

import java.io.IOException;

/**
 * This functional interface is using for getting paths for
 * class and it's root folders
 **/
public interface MethodRunnerPreProcessor {
    Class<?>[] getClasses(String projectRoot, String sourcesRoot, String[] classPaths)
            throws ClassNotFoundException, IOException;
}
