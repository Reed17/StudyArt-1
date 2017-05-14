package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.LessonRepository;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.utils.IO_utils.CourseIOUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final CourseIOUtils courseIOUtils;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository, CourseIOUtils courseIOUtils) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.courseIOUtils = courseIOUtils;
    }

    @Override
    public int addCourse(Course course){

        if (courseRepository.findByNameAndAuthor(course.getName(),course.getAuthor()) != null) {
            return -1;
        }
        return courseRepository.save(course).getId();
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

    @Override
    public int addLesson(Lesson lesson, int courseID) throws LessonsParsingException, GitAPIException, DirectoryCreatingException, IOException, LessonClassPathsException {

        Course course = courseRepository.findOne(courseID);

        String tempCourseLocalPath = courseIOUtils.saveCourseLocally(course.getUrl(), course.getName(), course.getId());

        String[] baseClassPaths = courseIOUtils.getLessonClassPaths(lesson.getBaseClasses(), lesson.getSourcesRoot());

        String[] testsClassPaths = courseIOUtils.getLessonClassPaths(lesson.getTestsClasses(), lesson.getTestsRoot());

        if (baseClassPaths.length == 0 || testsClassPaths.length == 0) {
            throw new LessonClassPathsException("No class paths in lesson");
        }

        lesson.setBaseClasses(Arrays.asList(baseClassPaths));
        lesson.setTestsClasses(Arrays.asList(baseClassPaths));

        //add validation

        List<Lesson> courseLessons = courseRepository.findOne(courseID).getLessons();
        if (courseLessons.contains(lesson)) {
            return -1;
        }

        lesson.setCourseID(courseID);

        courseLessons.add(lessonRepository.save(lesson));
        return lesson.getId();
    }
}
