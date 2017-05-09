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

    // list of completed lessons
    private List<Lesson> completedLessons;

    public Student(String login, String pass, String email) {
        super(login, pass, email);
        subscribed = new ArrayList<>();
        completed = new ArrayList<>();
        completedLessons = new ArrayList<>();
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

    public List<Lesson> getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(List<Lesson> completedLessons) {
        this.completedLessons = completedLessons;
    }

    public boolean subscribeTo(Course course) {
        return subscribed.add(course);
    }
}
