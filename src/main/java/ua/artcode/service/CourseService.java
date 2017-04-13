package ua.artcode.service;

import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.SolutionModel;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by v21k on 09.04.17.
 */

public interface CourseService {

    boolean addCourse(Course course) throws IOException;

    Course getCourse(int id) throws CourseNotFoundException;

    String runTask(String mainClass, int courseId);

    String checkSolution(String mainClass, int courseId, SolutionModel solution);

    Collection<Course> getAllCourses();
}
