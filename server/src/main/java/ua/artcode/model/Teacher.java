package ua.artcode.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ua.artcode.enums.UserType.TEACHER;

/**
 * Created by zhenia on 23.04.17.
 */
@Entity
public class Teacher extends User {

    private static final Map<String, Boolean> TEACHER_RIGHTS = createRightsMap();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TEACHER_CORSES",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "COURSE_ID")})
    private List<Course> courses;

    public Teacher(String login, String pass, String email) {
        super(login, pass, email, TEACHER);
        courses = new ArrayList<>();
    }

    public Teacher() {
        userType = TEACHER;
    }

    private static Map<String, Boolean> createRightsMap() {
        Map<String, Boolean> map = new ConcurrentHashMap<>();

        map.put("/courses/lessons/get", true);
        map.put("/courses/get", true);
        map.put("/courses/lessons/getAll", true);
        map.put("/courses/add", true);
        map.put("/run-class", true);
        map.put("/courses/lessons/run", true);
        map.put("/courses/lessons/add", true);
        map.put("/courses/lessons/send-solution-and-run-tests", true);

        return map;
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

    @Override
    public boolean isAccessable(String reqUrl) {
        return TEACHER_RIGHTS.get(reqUrl);
    }
}
