package ua.artcode.dao;

import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;

import java.util.Collection;

/**
 * Created by v21k on 09.04.17.
 */
public interface CourseDB {

    boolean add(Course course);

    boolean remove(Course course);

    Course getCourseByID(int id) throws CourseNotFoundException;

    String getCoursePath(Course course) throws CourseNotFoundException;

    Collection<Course> getAllCourses();
}
