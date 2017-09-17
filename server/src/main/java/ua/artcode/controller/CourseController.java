package ua.artcode.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.Lesson;
import ua.artcode.model.response.*;
import ua.artcode.service.CourseService;
import ua.artcode.service.RunService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CourseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    private final RunService runService;

    @Autowired
    public CourseController(CourseService courseService, RunService runService) {
        this.courseService = courseService;
        this.runService = runService;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Resource to get a course",
            response = Course.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/get", method = RequestMethod.GET)
    public Course getCourseByID(@RequestParam int id) throws AppException {

        Course course = courseService.getByID(id);
        LOGGER.info("Course get - OK, id {}", id);
        return course;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Resource to get all courses",
            response = List.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/getAll", method = RequestMethod.GET)
    public List<Course> getAllCourses() throws AppException {

        List<Course> courses = courseService.getAllCourses();
        LOGGER.info("Course get all - OK");
        return courses;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Resource to get a lesson",
            response = LessonResponse.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/get", method = RequestMethod.GET)
    public LessonResponse getLessonByID(@RequestParam int id, HttpServletRequest request) throws AppException, AuthenticationException {

        Integer userId = (Integer) request.getAttribute("User");

        if (userId == null) {
            throw new AuthenticationServiceException("Not authorised");
        }

        LessonResponse lesson = courseService.getLessonByID(id, userId);
        LOGGER.info("Lesson get - OK, id {}", id);
        return lesson;

    }

    @ApiOperation(httpMethod = "GET",
            value = "Resource to get all lessons of course(by id of any lesson)",
            response = List.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/getAllLessonsOfCourse", method = RequestMethod.GET)
    public List<FetchLessonsResponseEntity> getAllLessonsOfCourse(@RequestParam int id) throws AppException {

        List<FetchLessonsResponseEntity> responseList = courseService.getAllLessonsOfCourse(id);
        LOGGER.info("Lesson get all lesson of course - OK");

        return responseList;
    }


    @ApiOperation(httpMethod = "GET",
            value = "Resource to get all lesson",
            response = List.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/getAll", method = RequestMethod.GET)
    public List<Lesson> getAllLessons() throws AppException {
        List<Lesson> lessons = courseService.getAllLessons();
        LOGGER.info("Lesson get all - OK");
        return lessons;
    }


    @ApiOperation(httpMethod = "POST",
            value = "Resource to add a course",
            notes = "ID, lessons and localPath will be generated on server side, so you can password 0/null or default values",
            response = GeneralResponse.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/add", method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody @Valid Course course, HttpServletRequest request)
            throws SuchCourseAlreadyExists {

        Course result = courseService.addCourse(course);

        LOGGER.info("Course ADD - OK. Course (name - {}, author - {}, url - {})",
                result.getName(),
                result.getAuthor(),
                result.getUrl());
        return new GeneralResponse(ResponseType.INFO, "Course add - OK");
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to run class (external code)",
            notes = "Simple java class with main method",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "/run-class", method = RequestMethod.POST)
    public RunResults runClass(@RequestBody ExternalCode code, HttpServletRequest request) {
        try {
            RunResults results = runService.runMain(code);
            LOGGER.info("Run class (external source code) - OK");
            return results;

        } catch (Exception e) {
            LOGGER.error("Run class (external source code) - FAILED.", e);
            return new RunResults(new GeneralResponse(ResponseType.INFO, e.getMessage()));
        }
    }

    @ApiOperation(httpMethod = "GET",
            value = "Resource to run class from lesson with solution (need to add solution before)",
            notes = "Runs a tests for a certain lesson",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/send-solution-and-run-tests", method = RequestMethod.GET)
    public RunResults runLessonWithSolutionTests(@RequestParam int lessonId,
                                                 @RequestParam String url,
                                                 HttpServletRequest request) {

        try {
            RunResults results = runService.runLessonWithSolutionTests(lessonId, url);
            LOGGER.info("Run class with solution (lesson ID: {}) - OK", lessonId);
            return results;
        } catch (Exception e) {
            LOGGER.error("Run class from lesson with solution - FAILED", e);
            return new RunResults(new GeneralResponse(ResponseType.ERROR, e.getMessage()));
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to run class from lesson with solution (need to add solution before)",
            notes = "Runs a tests for a certain lesson",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/send-base-solution-and-run-tests", method = RequestMethod.POST)
    public RunResults runBaseLessonWithSolutionTests(@RequestParam int lessonId,
                                                 @RequestBody String classText,
                                                 HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("User");
        if (userId == null) {
            throw new AuthenticationServiceException("Not authorised!");
        }

        try {
            RunResults results = runService.runBaseLessonWithSolutionTests(lessonId, classText, userId);
            LOGGER.info("Run class with solution (lesson ID: {}) - OK", lessonId);
            return results;
        } catch (Exception e) {
            LOGGER.error("Run class from lesson with solution - FAILED", e);
            return new RunResults(new GeneralResponse(ResponseType.ERROR, e.getMessage()));
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to add lesson classes and description to course",
            notes = "Need to password description, courseID, source and tests class paths and roots, name",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/add", method = RequestMethod.POST)
    public GeneralResponse addLessonToCourse(@RequestParam int courseId,
                                             @RequestBody @Valid Lesson lesson,
                                             HttpServletRequest request) {
        try {
            Lesson result = courseService.addLesson(lesson, courseId);
            LOGGER.info("Add lesson (course ID: {}, lesson name: {}) - OK", courseId, lesson.getName());
            return new GeneralResponse(ResponseType.INFO, "Lesson add - OK");

        } catch (Exception e) {
            LOGGER.error("Add lesson to course - FAILED", e);
            return new GeneralResponse(ResponseType.ERROR, "Lesson already exists!");
        }
    }
}