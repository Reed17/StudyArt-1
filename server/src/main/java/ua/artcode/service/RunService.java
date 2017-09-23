package ua.artcode.service;

import ua.artcode.exceptions.AppException;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.response.RunResults;

import java.io.IOException;

/**
 * Created by v21k on 15.04.17.
 */
public interface RunService {
    RunResults runMain(ExternalCode code) throws Exception;

    RunResults runLessonWithSolutionTests(int lessonId, String url) throws Exception;

    RunResults runBaseLessonWithSolutionTests(int lessonId, String classText, Integer userId) throws AppException;
}
