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
    public int addCourse(Course course) {
        // todo why int, not Course? Return Course
        if (courseRepository.findByNameAndAuthor(course.getName(), course.getAuthor()) != null) {
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

        // todo if we already have this lesson, what should we do?
        // todo think about updateLesson method and corresponding query in LessonRepo
        Course course = courseRepository.findOne(courseID);


        // todo extract to method (setters and update)
        // save course and set roots
        String courseLocalPath = courseIOUtils.saveCourseLocally(course.getUrl(), course.getName(), course.getId());
        if(!course.getSourcesRoot().startsWith(courseLocalPath) && !course.getTestsRoot().startsWith(courseLocalPath)) {
            course.setSourcesRoot(courseLocalPath + course.getSourcesRoot());
            course.setTestsRoot(courseLocalPath + course.getTestsRoot());

            // update course so we can use roots later in RunService
            courseRepository.updateCourse(course.getSourcesRoot(), course.getTestsRoot(), course.getId());
        }


        Pair<List<String>, String> baseClasses =
                courseIOUtils.ensureLessonClassPathsAndRoot(
                        lesson.getBaseClasses(),
                        lesson.getSourcesRoot());

        Pair<List<String>, String> testClasses =
                courseIOUtils.ensureLessonClassPathsAndRoot(
                        lesson.getTestsClasses(),
                        lesson.getTestsRoot());


        lesson.setBaseClasses(baseClasses.getKey());
        lesson.setSourcesRoot(baseClasses.getValue());

        lesson.setTestsClasses(testClasses.getKey());
        lesson.setTestsRoot(testClasses.getValue());

        // todo validation

        List<Lesson> courseLessons = courseRepository.findOne(courseID).getLessons();
        if (courseLessons.contains(lesson)) {
            return -1; // todo why -1? return lesson back if ok, Exception otherwise. Error codes are for C developers :)
        }

        lesson.setCourseID(courseID);

        // todo did we update course? add lesson to it? and update in DB? see above my example update method for course
        courseLessons.add(lessonRepository.save(lesson));

        return lesson.getId();
    }


    @Override
    public Lesson getLessonByID(int id) {
        return lessonRepository.findOne(id);
    }
}
