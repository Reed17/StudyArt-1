package ua.artcode.service;

import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonNotFoundException;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.RunResults;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 15.04.17.
 */
public interface RunService {
    RunResults runMain(ExternalCode code) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    RunResults runLesson(int courseId, int lessonNumber) throws InvalidIDException, CourseNotFoundException, LessonNotFoundException, ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
