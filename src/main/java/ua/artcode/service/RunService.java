package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.CourseFromUser;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.response.RunResults;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 15.04.17.
 */
public interface RunService {
    RunResults runMain(ExternalCode code) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    RunResults runLesson(int courseId, int lessonNumber) throws InvalidIDException, CourseNotFoundException, LessonNotFoundException, ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    RunResults runLessonWithSolution(int courseId, int lessonNumber, ExternalCode code) throws InvalidIDException, CourseNotFoundException, LessonNotFoundException, ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    RunResults runLessonWithSolutionTests(int courseId, int lessonNumber, CourseFromUser userCource)
            throws InvalidIDException,
            CourseNotFoundException,
            LessonNotFoundException,
            ClassNotFoundException,
            IOException,
            InvocationTargetException,
            IllegalAccessException,
            NoSuchMethodException, GitAPIException, DirectoryCreatingException;
}
