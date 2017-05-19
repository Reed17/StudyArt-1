package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exceptions.AppException;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by v21k on 15.04.17.
 */
public interface CourseService {
    int addCourse(Course course);

    Course getByID(int id) throws InvalidIDException, CourseNotFoundException;

    boolean removeCourse(int id) throws InvalidIDException;

    Collection<Course> getAll();

    int addLesson(Lesson lesson, int courceID) throws GitAPIException, IOException, AppException;

    Lesson getLessonByID(int id);
}
