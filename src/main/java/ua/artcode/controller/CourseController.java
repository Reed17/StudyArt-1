package ua.artcode.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.GeneralResponse;
import ua.artcode.model.RunResults;
import ua.artcode.service.CourseService;
import ua.artcode.service.RunService;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@RestController
public class CourseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private RunService runService;

    @RequestMapping(value = "/courses/get", method = RequestMethod.GET)
    public Course getCourseByID(@RequestParam int id) throws InvalidIDException, CourseNotFoundException {
        Course course = courseService.getByID(id);
        LOGGER.info("Course get - OK, id {}", id);
        return course;
    }

    @RequestMapping(value = "/courses/add", method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody @Valid Course course) {
        try {
            boolean result = courseService.addCourse(course);

            LOGGER.info("Course ADD - OK. Course (name - {}, author - {}, url - {})",
                    course.getName(), course.getAuthor(), course.getUrl());

            return result ? new GeneralResponse("OK") : new GeneralResponse("FAIL");

        } catch (GitAPIException | DirectoryCreatingException | LessonsParsingException e) {

            LOGGER.error("Course add - FAILED", e);

            return new GeneralResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/run-class", method = RequestMethod.POST)
    public RunResults runClass(@RequestBody ExternalCode code) {
        try {
            RunResults results = runService.runMain(code);
            LOGGER.info("Run class (external source code) - OK");
            return results;
        } catch (ClassNotFoundException |
                IOException |
                InvocationTargetException |
                IllegalAccessException |
                NoSuchMethodException e) {
            LOGGER.error("Run class (external source code) - FAILED.", e);
            return new RunResults(e.getMessage());
        }
    }

    @RequestMapping(value = "/courses/lessons/run")
    public RunResults runLesson(@RequestParam int courseId,
                                @RequestParam int lessonNumber) {
        try {
            RunResults results = runService.runLesson(courseId, lessonNumber);
            LOGGER.info("Run class (course ID: {}, lesson number: {}) - OK", courseId, lessonNumber);
            return results;
        } catch (InvalidIDException |
                CourseNotFoundException |
                ClassNotFoundException |
                LessonNotFoundException |
                InvocationTargetException |
                IOException |
                IllegalAccessException |
                NoSuchMethodException e) {
            LOGGER.error("Run class from lesson - FAILED", e);
            return new RunResults(e.getMessage());
        }
    }

    @RequestMapping(value = "courses/lessons/send-solution-and-run", method = RequestMethod.POST)
    public RunResults runLessonWithSolution(@RequestParam int courseId,
                                            @RequestParam int lessonNumber,
                                            @RequestBody ExternalCode code) {
        try {
            RunResults results = runService.runLessonWithSolution(courseId, lessonNumber, code);
            LOGGER.info("Run class with solution (course ID: {}, lesson number: {}) - OK", courseId, lessonNumber);
            return results;
        } catch (InvalidIDException |
                CourseNotFoundException |
                ClassNotFoundException |
                LessonNotFoundException |
                InvocationTargetException |
                IOException |
                NoSuchMethodException |
                IllegalAccessException e) {
            LOGGER.error("Run class from lesson with solution - FAILED", e);
            return new RunResults(e.getMessage());
        }
    }
}
