package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.StudyArtDB;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonsParsingException;
import ua.artcode.model.Course;

import java.util.Collection;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private StudyArtDB courseDB;

    @Override
    public boolean addCourse(Course course) throws DirectoryCreatingException, LessonsParsingException, GitAPIException {
        return courseDB.addCourse(course);
    }

    @Override
    public Course getByID(int id) throws InvalidIDException, CourseNotFoundException {
        return courseDB.getCourseByID(id);
    }

    @Override
    public boolean removeCourse(int id) throws InvalidIDException {
        return courseDB.removeCourse(id);
    }

    @Override
    public Collection<Course> getAll() {
        return courseDB.getAllCourses();
    }
}
