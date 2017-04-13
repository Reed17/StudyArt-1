package ua.artcode.dao;

import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by v21k on 09.04.17.
 */

@Component
public class CourseDBImpl implements CourseDB {
    private Map<Integer, Course> courses;

    public CourseDBImpl() {
        this.courses = new HashMap<>();
    }

    @Override
    public boolean add(Course course) {
        if (course.getId() == 0){
            course.setId(courses.size()+1);
        }
        if (!courses.values().contains(course)) {
            courses.put(course.getId(), course);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Course course) {
        return courses.remove(course.getId()) != null;
    }

    @Override
    public Course getCourseByID(int id) throws CourseNotFoundException {
        return courses.values()
                .stream()
                .filter(course -> course.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("No course with id " + id));
    }

    @Override
    public String getCoursePath(Course course) throws CourseNotFoundException {
        return courses.values()
                .stream()
                .filter(c -> c.equals(course))
                .map(Course::getCourseLocalPath)
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("No course found"));
    }

    @Override
    public Collection<Course> getAllCourses() {
        return courses.values();
    }
}
