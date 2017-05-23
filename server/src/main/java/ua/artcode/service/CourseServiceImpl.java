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
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.ResultChecker;
import ua.artcode.utils.ValidationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static ua.artcode.utils.StringUtils.*;
/**
 * Created by v21k on 15.04.17.
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final CourseIOUtils courseIOUtils;
    private final ValidationUtils validationUtils;
    private final ResultChecker resultChecker;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository,
                             CourseIOUtils courseIOUtils, CommonIOUtils commonIOUtils,
                             ValidationUtils validateFiles, ResultChecker resultChecker) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.courseIOUtils = courseIOUtils;
        this.validationUtils = validateFiles;
        this.resultChecker = resultChecker;
    }

    @Override
    public Course addCourse(Course course) throws SuchCourseAlreadyExists {
        if (courseRepository.findByNameAndAuthor(course.getName(), course.getAuthor()) != null) {
            throw new SuchCourseAlreadyExists("This course already in database!");
        }
        return courseRepository.save(course);
    }

    @Override
    public Course getByID(int id) throws UnexpectedNullException {
        Course result = courseRepository.findOne(id);
        resultChecker.checkNull(result, "No course found with id: " + id);

        return result;
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
    public Lesson addLesson(Lesson lesson, int courseID) throws GitAPIException, IOException, AppException {

        // todo if we already have this lesson, what should we do?
        // todo think about updateLesson method and corresponding query in LessonRepo
        Course course = getByID(courseID);

        // save locally for further operations
        String courseLocalPath = courseIOUtils.saveCourseLocally(course.getUrl(), course.getName(), course.getId());

        if (!lesson.getSourcesRoot().isEmpty()) {
            lesson.setSourcesRoot(normalizePath(checkStartsWithAndAppend(lesson.getSourcesRoot(), courseLocalPath)));
        } else if (!lesson.getTestsRoot().isEmpty()) {
            lesson.setTestsRoot(normalizePath(checkStartsWithAndAppend(lesson.getTestsRoot(), courseLocalPath)));
        }

        // update root packages
        courseIOUtils.updateCoursePaths(courseLocalPath, course);

        // update dependencies
        courseIOUtils.updateCourseDependencies(courseLocalPath, course);

        // todo maybe there is another solution, more simple?
        Pair<List<String>, String> baseClasses =
                courseIOUtils.ensureLessonClassPathsAndRoot(
                        lesson.getBaseClasses(),
                        lesson.getSourcesRoot(),
                        courseLocalPath);

        Pair<List<String>, String> testClasses =
                courseIOUtils.ensureLessonClassPathsAndRoot(
                        lesson.getTestsClasses(),
                        lesson.getTestsRoot(),
                        courseLocalPath);


        lesson.setBaseClasses(baseClasses.getKey());
        lesson.setSourcesRoot(baseClasses.getValue());

        lesson.setTestsClasses(testClasses.getKey());
        lesson.setTestsRoot(testClasses.getValue());

        // check if exists
        validationUtils.validateFiles(
                lesson.getBaseClasses(),
                lesson.getTestsClasses(),
                lesson.getRequiredClasses());

        List<Lesson> courseLessons = courseRepository.findOne(courseID).getLessons();
        if (courseLessons.contains(lesson)) { // todo check equals and hash
            throw new SuchLessonAlreadyExist("Such lesson already exist!");
        }

        lesson.setCourseID(courseID);

        // todo did we update course? add lesson to it? and update in DB? see above my example update method for course
        courseLessons.add(lessonRepository.save(lesson));

        return lesson;
    }

    @Override
    public Lesson getLessonByID(int id) throws UnexpectedNullException {
        Lesson result = lessonRepository.findOne(id);
        resultChecker.checkNull(result, "Lesson not found!");

        return result;
    }

    @Override
    public List<Lesson> getAllLessons(){
        return StreamSupport.stream(lessonRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getAllCourses() {
        return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
