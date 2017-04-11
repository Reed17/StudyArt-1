package ua.artcode.dao;

import ua.artcode.model.Course;

/**
 * Created by v21k on 09.04.17.
 */
public interface CourseDB {

    boolean add(Course course, String path);

    boolean remove(int id, Course course);

    Course get(int id);

    String getCoursePath(Course course);
}
