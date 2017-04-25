package ua.artcode.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenia on 23.04.17.
 */
public class Teacher extends User {

    //TODO add more cpecific fields

    // list of courses, which created this teacher
    private List<Course> courses;

    public Teacher(String login, String pass, String email) {
        super(login, pass, email);
        courses = new ArrayList<>();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public boolean addCourse(Course course) {
        return courses.add(course);
    }
}
