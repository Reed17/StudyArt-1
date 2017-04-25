package ua.artcode.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenia on 23.04.17.
 */
public class Student extends User {

    // list of courses, to which subscribed student
    private List<Course> subscribed;

    // list of completed courses
    private List<Course> completed;

    public Student(String login, String pass, String email) {
        super(login, pass, email);
        subscribed = new ArrayList<>();
        completed = new ArrayList<>();
    }

    public List<Course> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<Course> subscribed) {
        this.subscribed = subscribed;
    }

    public List<Course> getCompleted() {
        return completed;
    }

    public void setCompleted(List<Course> completed) {
        this.completed = completed;
    }

    public boolean subscribeTo(Course course) {
        return subscribed.add(course);
    }
}
