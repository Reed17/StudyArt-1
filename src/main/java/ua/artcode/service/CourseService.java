package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonsParsingException;
import ua.artcode.model.Course;

import java.util.Collection;

/**
 * Created by v21k on 15.04.17.
 */
public interface ICourseService {
    boolean addCourse(Course course) throws DirectoryCreatingException, LessonsParsingException, GitAPIException;

    Course getByID(int id) throws InvalidIDException, CourseNotFoundException;

    boolean removeCourse(int id) throws InvalidIDException;

    Collection<Course> getAll();
}
