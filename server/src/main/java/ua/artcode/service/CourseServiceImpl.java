package ua.artcode.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ua.artcode.utils.StringUtils.checkStartsWithAndAppend;
import static ua.artcode.utils.StringUtils.normalizePath;

/**
 * Created by v21k on 15.04.17.
 */
@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

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

        // todo make more simple
        Course course = getByID(courseID);

        // save locally for further operations
        String courseLocalPath = courseIOUtils.saveCourseLocally(course.getUrl(), course.getName(), course.getId());

        // update source and test root paths for lesson
        if (!lesson.getSourcesRoot().isEmpty()) {
            lesson.setSourcesRoot(normalizePath(checkStartsWithAndAppend(lesson.getSourcesRoot(), courseLocalPath)));
        }
        if (!lesson.getTestsRoot().isEmpty()) {
            lesson.setTestsRoot(normalizePath(checkStartsWithAndAppend(lesson.getTestsRoot(), courseLocalPath)));
        }

        // update source and test root paths for course
        if (!course.getSourcesRoot().startsWith(courseLocalPath) && !course.getTestsRoot().startsWith(courseLocalPath)) {
            course.setSourcesRoot(courseLocalPath + course.getSourcesRoot());
            course.setTestsRoot(courseLocalPath + course.getTestsRoot());
        }

        // save dependencies, parse and update in course
        courseIOUtils.saveMavenDependenciesLocally(courseLocalPath);
        course.setDependencies(courseIOUtils.copyDependencies(courseLocalPath));

        // parse base and test classes
        AbstractMap.SimpleEntry<List<String>, String> baseClasses =
                courseIOUtils.ensureLessonClassPathsAndRoot(
                        lesson.getBaseClasses(),
                        lesson.getSourcesRoot(),
                        courseLocalPath);

        AbstractMap.SimpleEntry<List<String>, String> testClasses =
                courseIOUtils.ensureLessonClassPathsAndRoot(
                        lesson.getTestsClasses(),
                        lesson.getTestsRoot(),
                        courseLocalPath);


        lesson.setBaseClasses(baseClasses.getKey());
        lesson.setSourcesRoot(baseClasses.getValue());

        lesson.setTestsClasses(testClasses.getKey());
        lesson.setTestsRoot(testClasses.getValue());

        // check if files exist
        validationUtils.validateFiles(
                lesson.getBaseClasses(),
                lesson.getTestsClasses());

        List<Lesson> courseLessons = course.getLessons();
        if (courseLessons.contains(lesson)) {
            String message = "Such lesson already exist!";
            SuchLessonAlreadyExist suchLessonAlreadyExist = new SuchLessonAlreadyExist(message);

            LOGGER.error(message, suchLessonAlreadyExist);
            throw suchLessonAlreadyExist;
        } else {
            lesson.setCourseID(course.getId());
            courseLessons.add(lesson);
            course.setLessons(courseLessons);
        }

        // delete all downloaded files
        FileUtils.deleteDirectory(new File(courseLocalPath));

        courseRepository.save(course);
        return lesson;
    }

    @Override
    public Lesson getLessonByID(int id) throws LessonNotFoundException {
        Lesson result = lessonRepository.findOne(id);

        if (result == null) {
            String message = "Lesson with id " + id + " not found.";
            LessonNotFoundException lessonNotFoundException = new LessonNotFoundException(message);

            LOGGER.error(message, lessonNotFoundException);
            throw lessonNotFoundException;
        }

        return result;
    }

    @Override
    public Integer getPreviousLessonId(int id) throws LessonNotFoundException, UnexpectedNullException {
        Lesson lesson = getLessonByID(id);
        List<Lesson> courseLessons = getByID(lesson.getCourseID()).getLessons();

        int ind = courseLessons.indexOf(lesson);

        return (ind - 1 < 0) ? null : courseLessons.get(ind - 1).getId();
    }

    @Override
    public Integer getNextLessonId(int id) throws UnexpectedNullException, LessonNotFoundException {

        Lesson lesson = getLessonByID(id);
        List<Lesson> courseLessons = getByID(lesson.getCourseID()).getLessons();
        int ind = courseLessons.indexOf(lesson);

        return (ind < 0 || ind >= (courseLessons.size() - 1)) ? null : courseLessons.get(ind + 1).getId();
    }

    @Override
    public List<Lesson> getAllLessons() {
        return StreamSupport.stream(lessonRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> getAllCourses() {
        return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
