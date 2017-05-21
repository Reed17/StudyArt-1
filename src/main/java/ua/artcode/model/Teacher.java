package ua.artcode.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhenia on 23.04.17.
 */
@Entity
public class Teacher extends User {

    //TODO add more cpecific fields

    private static final Map<String, Boolean> TEACHER_RIGHTS = createRightsMap();

    private static Map<String,Boolean> createRightsMap() {
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

    // list of courses, which created this teacher
    @OneToMany(cascade = CascadeType.ALL)
    private List<Course> courses;

    public Teacher(String login, String pass, String email) {
        super(login, pass, email);
        courses = new ArrayList<>();
    }

    public Teacher() {
    }

    ;

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
