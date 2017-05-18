package ua.artcode.service;

import ua.artcode.model.CourseFromUser;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.response.RunResults;

/**
 * Created by v21k on 15.04.17.
 */
public interface RunService {
    RunResults runMain(ExternalCode code) throws Exception;

    RunResults runLessonWithSolutionTests(int courseID, int lessonNumber, CourseFromUser userCource) throws Exception;
}
