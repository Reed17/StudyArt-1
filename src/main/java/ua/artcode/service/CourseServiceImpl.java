package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonsParsingException;
import ua.artcode.model.Course;
import ua.artcode.utils.IO_utils.CourseIOUtils;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseIOUtils courseIOUtils;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CourseIOUtils courseIOUtils) {
        this.courseRepository = courseRepository;
        this.courseIOUtils = courseIOUtils;
    }

    @Override
    public boolean addCourse(Course course) throws DirectoryCreatingException, LessonsParsingException, GitAPIException {
        try {
            course.setLocalPath(courseIOUtils.saveLocally(course.getUrl(), course.getName(), course.getId()));
            course.setLessons(courseIOUtils.getLessons(course));
        } catch (IOException e) {
            throw new LessonsParsingException("Can't parse lessons for course: " + course.getName());
        }
        courseRepository.save(course);
        return true;
    }

    @Override
    public Course getByID(int id) throws InvalidIDException, CourseNotFoundException {
        return courseRepository.findOne(id);
    }

    @Override
    public boolean removeCourse(int id) throws InvalidIDException {
        courseRepository.delete(id);
        return true;
    }

    @Override
    public Collection<Course> getAll() {
        return (Collection<Course>) courseRepository.findAll();
    }
}
