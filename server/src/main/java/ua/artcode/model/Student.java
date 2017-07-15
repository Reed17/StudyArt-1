package ua.artcode.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static ua.artcode.enums.UserType.STUDENT;

/**
 * Created by zhenia on 23.04.17.
 */
@Entity
@Table(name = "STUDENTS")
public class Student extends User {

    private static final Map<String, Boolean> STUDENT_RIGHTS = createRightsMap();


//    private Map<Integer, String> userCourseCopies = new HashMap<>(); // todo course copies
    // course1 --- git... or local path
    // course2 --- git...

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "STUDENT_SUBSCRIBED",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "COURSE_ID")})
    private Set<Course> subscribed;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "STUDENT_COMPLETED",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "COURSE_ID")})
    private Set<Course> completed;

    public Student() {
        userType = STUDENT;
    }

    public Student(String login, String pass, String email) {
        super(login, pass, email, STUDENT);
        subscribed = new HashSet<>();
        completed = new HashSet<>();
    }

    private static Map<String, Boolean> createRightsMap() {
        Map<String, Boolean> map = new ConcurrentHashMap<>();

        map.put("/courses/lessons/get", true);
        map.put("/courses/get", true);
        map.put("/courses/lessons/getAll", true);
        map.put("/courses/add", false);
        map.put("/run-class", true);
        map.put("/courses/lessons/run", true);
        map.put("/courses/lessons/add", false);
        map.put("/courses/lessons/send-solution-and-run-tests", true);

        return map;
    }

    public Set<Course> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Set<Course> subscribed) {
        this.subscribed = subscribed;
    }

    public Set<Course> getCompleted() {
        return completed;
    }

    public void setCompleted(Set<Course> completed) {
        this.completed = completed;
    }

    public boolean subscribeTo(Course course) {
        return !subscribed.contains(course) && subscribed.add(course);
    }

    @Override
    public boolean isAccessable(String reqUrl) {
        return STUDENT_RIGHTS.get(reqUrl);
    }
}
