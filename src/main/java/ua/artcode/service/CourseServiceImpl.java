package ua.artcode.service;

import javafx.util.Pair;
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

        Pair<List<String>, String> baseClasses = courseIOUtils.ensureLessonClassPathsAndRoot(lesson.getBaseClasses(), lesson.getSourcesRoot());
        Pair<List<String>, String> testClasses = courseIOUtils.ensureLessonClassPathsAndRoot(lesson.getTestsClasses(), lesson.getTestsRoot());

        lesson.setBaseClasses(baseClasses.getKey());
        lesson.setSourcesRoot(baseClasses.getValue());
        lesson.setTestsClasses(testClasses.getKey());
        lesson.setTestsRoot(testClasses.getValue());

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
