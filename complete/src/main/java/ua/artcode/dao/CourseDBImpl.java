package ua.artcode.dao;

import ua.artcode.model.Course;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v21k on 09.04.17.
 */

@Component
public class CourseDBImpl implements CourseDB {
    private Map<Integer, Course> courses;
    private Map<Course, String> coursesPaths;
    private static int counter = 0;

    public CourseDBImpl() {
        this.courses = new HashMap<>();
        this.coursesPaths = new HashMap<>();
    }

    public CourseDBImpl(Map<Integer, Course> courses, Map<Course, String> coursesPaths) {
        this.courses = courses;
        this.coursesPaths = coursesPaths;
    }

    @Override
    public boolean add(Course course, String path) {
        if (!courses.values().contains(course)) {
            courses.put(++counter, course);
            coursesPaths.put(course, path);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(int id, Course course) {

        return courses.remove(id) != null && coursesPaths.remove(course) != null;
    }

    @Override
    public Course get(int id) {
        return courses.get(id);
    }

    @Override
    public String getCoursePath(Course course){
        return coursesPaths.get(course);
    }
}
