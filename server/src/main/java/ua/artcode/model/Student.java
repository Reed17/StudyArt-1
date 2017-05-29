package ua.artcode.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ua.artcode.enums.UserType.STUDENT;

/**
 * Created by zhenia on 23.04.17.
 */
@Entity
public class Student extends User {

    private static final Map<String, Boolean> STUDENT_RIGHTS = createRightsMap();

    private static Map<String,Boolean> createRightsMap() {
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

    // list of courses, to which subscribed student
    @OneToMany(cascade = CascadeType.ALL)
    private List<Course> subscribed;

    // list of completed courses
    @OneToMany(cascade = CascadeType.ALL)
    private List<Course> completed;

    public Student() {
        userType = STUDENT;
    }

    ;

    public Student(String login, String pass, String email) {
        super(login, pass, email);
        userType = STUDENT;
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

    @Override
    public boolean isAccessable(String reqUrl) {
        return STUDENT_RIGHTS.get(reqUrl);
    }
}
