package ua.artcode.utils;

import ua.artcode.model.SolutionModel;

/**
 * Created by v21k on 14.04.17.
 */
public class StringUtils {
    /**
     * Appends solution to source Class (as String)
     *
     * @param source   - source class in String format
     * @param solution SolutionModel with only 1 fields - solution(String)
     * @return updated String
     */
    public static String addSolution(String source, SolutionModel solution) {
        return source.substring(0, source.lastIndexOf("}"))
                + solution.getSolution() + "}";
    }
}
