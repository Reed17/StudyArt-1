package ua.artcode.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exceptions.AppException;
import ua.artcode.model.Course;
import ua.artcode.model.CourseFromUser;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.Lesson;
import ua.artcode.model.response.GeneralResponse;
import ua.artcode.model.response.ResponseType;
import ua.artcode.model.response.RunResults;
import ua.artcode.service.CourseService;
import ua.artcode.service.RunService;

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
            value = "Resource to get a lesson",
            response = Lesson.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/get", method = RequestMethod.GET)
    public Lesson getLessonByID(@RequestParam int id) throws AppException {

        Lesson lesson = courseService.getLessonByID(id);
        LOGGER.info("Lesson get - OK, id {}", id);
        return lesson;

    }

    @ApiOperation(httpMethod = "GET",
            value = "Resource to get all esson",
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
            notes = "ID, lessons and localPath will be generated on server side, so you can pass 0/null or default values",
            response = GeneralResponse.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/add", method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody @Valid Course course) {
        Course result = courseService.addCourse(course);
        if (course == null) {
            LOGGER.info("Course ADD - OK. Course (name - {}, author - {}, url - {})",
                    course.getName(),
                    course.getAuthor(),
                    course.getUrl());
            return new GeneralResponse(ResponseType.INFO, "Course add - OK");
        } else {
            LOGGER.error("Course add - FAILED", "Course already exists!");
            return new GeneralResponse(ResponseType.ERROR, "Course already exists!");
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to run class (external code)",
            notes = "Simple java class with main method",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "/run-class", method = RequestMethod.POST)
    public RunResults runClass(@RequestBody ExternalCode code) {
        try {

            RunResults results = runService.runMain(code);
            LOGGER.info("Run class (external source code) - OK");
            return results;

        } catch (Exception e) {

            LOGGER.error("Run class (external source code) - FAILED.", e);
            return new RunResults(new GeneralResponse(ResponseType.INFO, e.getMessage()));
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to run class from lesson with solution (need to add solution before)",
            notes = "Runs a tests for a certain lesson",
            response = GeneralResponse.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/send-solution-and-run-tests", method = RequestMethod.POST)
    public RunResults runLessonWithSolutionTests(@RequestParam int courseId,
                                                 @RequestParam int lessonNumber,
                                                 @RequestBody @Valid CourseFromUser userCourse) {
        try {

            RunResults results = runService.runLessonWithSolutionTests(courseId, lessonNumber, userCourse);
            LOGGER.info("Run class with solution (course ID: {}, lesson number: {}) - OK", courseId, lessonNumber);
            return results;

        } catch (Exception e) {

            LOGGER.error("Run class from lesson with solution - FAILED", e);
            return new RunResults(new GeneralResponse(ResponseType.ERROR, e.getMessage()));
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to add lesson classes and description to course",
            notes = "Need to pass description, courseID, source and tests class paths and roots, name",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/add", method = RequestMethod.POST)
    public GeneralResponse addLessonToCourse(@RequestParam int courseId,
                                             @RequestBody @Valid Lesson lesson) {
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