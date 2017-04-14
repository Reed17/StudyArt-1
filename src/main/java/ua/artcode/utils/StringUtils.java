package ua.artcode.utils;

import ua.artcode.model.SolutionModel;

/**
 * Created by v21k on 14.04.17.
 */
public class StringUtils {
    public static String addSoulution(String source, SolutionModel solution) {
        return source.substring(0, source.lastIndexOf("}"))
                + solution.getSolution() + "}";
    }
}
