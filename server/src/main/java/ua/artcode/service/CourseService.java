package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.model.response.FetchLessonsResponseEntity;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by v21k on 15.04.17.
 */
public interface CourseService {
    Course addCourse(Course course) throws SuchCourseAlreadyExists;

    Course getByID(int id) throws InvalidIDException, CourseNotFoundException, UnexpectedNullException;

    boolean removeCourse(int id) throws InvalidIDException;

    Collection<Course> getAll();

    Lesson addLesson(Lesson lesson, int courseID) throws GitAPIException, IOException, AppException, ValidationException;

    Lesson getLessonByID(int id) throws UnexpectedNullException, LessonNotFoundException;

    List<FetchLessonsResponseEntity> getAllLessonsOfCourse(int id) throws LessonNotFoundException, UnexpectedNullException;

    List<Lesson> getAllLessons();

    List<Course> getAllCourses();
}
