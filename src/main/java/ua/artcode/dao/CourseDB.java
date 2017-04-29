package ua.artcode.dao;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.utils.IO_utils.CourseIOUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by v21k on 15.04.17.
 */
@Component
public class CourseDB implements StudyArtDB {
    private Map<Integer, Course> courseMap;

    @Autowired
    private CourseIOUtils courseIOUtils;

    public CourseDB() {
        this.courseMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean addCourse(Course course) throws GitAPIException, DirectoryCreatingException, LessonsParsingException {
        if (containsCourse(course)) {
            return updateCourse(course);
        }
        try {
            course.setId(courseMap.size() + 1);
            course.setLocalPath(courseIOUtils.saveLocally(course.getUrl(), course.getName(), course.getId()));
            course.setLessons(courseIOUtils.getLessons(course));
        } catch (IOException e) {
            throw new LessonsParsingException("Can't parse lessons for course: " + course.getName());
        }

        return !courseMap.values().contains(course) && courseMap.put(course.getId(), course) == null;
    }

    @Override
    public boolean updateCourse(Course course) {
        return courseMap.put(course.getId(), course) != null;
    }

    @Override
    public boolean removeCourse(int id) throws InvalidIDException {
        checkID(id);
        return courseMap.remove(id) != null;
    }

    @Override
    public boolean containsCourse(Course course) {
        return courseMap.values().contains(course);
    }

    @Override
    public Collection<Course> getAllCourses() {
        return courseMap.values();
    }

    @Override
    public Course getCourseByID(int id) throws InvalidIDException, CourseNotFoundException {
        checkID(id);
        return courseMap.values()
                .stream()
                .filter(course -> course.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("No course found with id: " + id));
    }

    /**
     * Lesson packages starts with "_[number]_"
     * For numbers < 10 it looks like "_02_lesson" etc, so if lesson number is < 10,
     * we need to addCourse "0" to it's String.valueOf()
     * Otherwise - do not addCourse anything.
     */
    @Override
    public Lesson getLesson(int lessonNumber, Course course) throws LessonNotFoundException {
        return course.getLessons()
                .stream()
                .filter(lsn -> lsn.getName().contains(lessonNumber < 10 ? "0" + lessonNumber : String.valueOf(lessonNumber)))
                .findFirst()
                .orElseThrow(() -> new LessonNotFoundException("No lesson found with number :" + lessonNumber));
    }

    private void checkID(int id) throws InvalidIDException {
        if (id <= 0) {
            throw new InvalidIDException("ID must be > 0");
        }
    }
}
