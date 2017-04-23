package ua.artcode.dao;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;

import java.util.Collection;

/**
 * Created by v21k on 15.04.17.
 */
public interface StudyArtDB {
    boolean addCourse(Course value) throws GitAPIException, DirectoryCreatingException, LessonsParsingException;

    boolean updateCourse(Course value);

    boolean removeCourse(int id) throws InvalidIDException;

    boolean containsCourse(Course value);

    Collection<Course> getAllCourses();

    Course getCourseByID(int id) throws InvalidIDException, CourseNotFoundException;

    Lesson getLesson(int lessonNumber, Course course) throws LessonNotFoundException;
}
