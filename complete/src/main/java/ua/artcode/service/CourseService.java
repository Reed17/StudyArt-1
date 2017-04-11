package ua.artcode.service;

import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;

import java.io.IOException;

/**
 * Created by v21k on 09.04.17.
 */

public interface CourseService {

    boolean addCourse(Course course) throws IOException;

    Course getCourse(int id) throws CourseNotFoundException;

    public boolean runTask(String mainClass, int courseId);
}
