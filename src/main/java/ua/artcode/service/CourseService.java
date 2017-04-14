package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exception.CourseDirectoryCreatingExcpetion;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.exception.NoSuchDirectoryException;
import ua.artcode.model.CheckResult;
import ua.artcode.model.Course;
import ua.artcode.model.SolutionModel;

import java.util.Collection;

/**
 * Created by v21k on 09.04.17.
 */

public interface CourseService {

    boolean addCourseFromGit(Course course) throws CourseDirectoryCreatingExcpetion, GitAPIException;

    Course getCourse(int id) throws CourseNotFoundException;

    CheckResult runClass(String packageName, String mainClass, int courseId)
            throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException;

    CheckResult sendSolution(String packageName,
                             String mainClass,
                             int courseId,
                             SolutionModel solution)
            throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException;

    Collection<Course> getAllCourses();
}
