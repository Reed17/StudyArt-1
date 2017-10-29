package ua.artcode.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.LessonRepository;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.model.Student;
import ua.artcode.model.UserCourseCopy;
import ua.artcode.model.response.FetchLessonsResponseEntity;
import ua.artcode.model.response.LessonResponse;
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.ResultChecker;
import ua.artcode.utils.ValidationUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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
    private final StudentRepository studentRepository;
    private final CourseIOUtils courseIOUtils;
    private final ValidationUtils validationUtils;
    private final ResultChecker resultChecker;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository,
                             CourseIOUtils courseIOUtils, CommonIOUtils commonIOUtils,
                             ValidationUtils validateFiles, ResultChecker resultChecker,
                             StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.courseIOUtils = courseIOUtils;
        this.validationUtils = validateFiles;
        this.resultChecker = resultChecker;
        this.studentRepository = studentRepository;
    }

    @Override
    public Course addCourse(Course course) throws SuchCourseAlreadyExists {
        if (courseRepository.findByNameAndAuthor(course.getName(), course.getAuthor()) != null) {
            throw new SuchCourseAlreadyExists("This course already in database!");
        }

        course = courseRepository.save(course);

        try {
            course.setLocalPath(courseIOUtils.saveCourseLocally(course.getUrl(), course.getName(), course.getId()));

            LOGGER.info("123123123123");
            LOGGER.info(course.getLocalPath());

        } catch (GitAPIException | DirectoryCreatingException e) {
            LOGGER.error(e.getMessage());
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
        if (!course.getSourcesRoot().startsWith(courseLocalPath)
                && !course.getTestsRoot().startsWith(courseLocalPath)) {

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

        courseRepository.save(course);
        return lesson;
    }

    @Override
    public LessonResponse getLessonByID(int id, int userId) throws LessonNotFoundException {
        Lesson result = lessonRepository.findOne(id);

        if (result == null) {
            String message = "Lesson with id " + id + " not found.";
            LessonNotFoundException lessonNotFoundException = new LessonNotFoundException(message);

            LOGGER.error(message, lessonNotFoundException);
            throw lessonNotFoundException;
        }


        List<String> classes = new ArrayList<>();

        Student student = studentRepository.findOne(userId);
        UserCourseCopy copy = student.getUserCourseCopies().get(result.getCourseID());

        if (copy == null) {
            Course course = courseRepository.findOne(result.getCourseID());

            result.getBaseClasses().forEach(c -> {
                try {
                    classes.add(new String(Files.readAllBytes(
                            Paths.get(courseIOUtils.getValidPathForCourseLesson(c, course.getLocalPath())))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } else {
            String userSource = copy.getPath();

            result.getBaseClasses().forEach(c -> {
                try {
                    classes.add(new String(Files.readAllBytes(
                            Paths.get(courseIOUtils.getValidPathForUsersCourse(c, userSource)))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


        return new LessonResponse(
                result.getId(), result.getName(), classes, result.getDescription(), result.getTheory());
    }

    @Override
    public Lesson getLessonByID(int id) throws UnexpectedNullException, LessonNotFoundException {
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
    public List<FetchLessonsResponseEntity> getAllLessonsOfCourse(int id)
            throws LessonNotFoundException, UnexpectedNullException {

        return getByID(lessonRepository.findOne(id).getCourseID()).getLessons()
                .stream().map(l -> new FetchLessonsResponseEntity(l.getName(), l.getId()))
                .collect(Collectors.toList());
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
