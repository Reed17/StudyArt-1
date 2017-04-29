package ua.artcode.controller;

import io.swagger.annotations.ApiOperation;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exceptions.AppException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.LessonsParsingException;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.response.GeneralResponse;
import ua.artcode.model.response.ResponseType;
import ua.artcode.model.response.RunResults;
import ua.artcode.service.CourseService;
import ua.artcode.service.RunService;

import javax.validation.Valid;

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

    @ApiOperation(httpMethod = "POST",
            value = "Resource to add a course",
            notes = "ID, lessons and localPath will be generated on server side, so you can pass 0/null or default values",
            response = GeneralResponse.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/add", method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody @Valid Course course) {
        try {

            boolean result = courseService.addCourse(course);
            LOGGER.info("Course ADD - OK. Course (name - {}, author - {}, url - {})",
                    course.getName(),
                    course.getAuthor(),
                    course.getUrl());
            return new GeneralResponse(ResponseType.INFO, "Course add - OK");

        } catch (GitAPIException | DirectoryCreatingException | LessonsParsingException e) {

            LOGGER.error("Course add - FAILED", e);
            return new GeneralResponse(ResponseType.ERROR, e.getMessage());
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

    @ApiOperation(httpMethod = "GET",
            value = "Resource to run class from lesson",
            notes = "Runs a class in certain lesson (class must have main method)",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "/courses/lessons/run")
    public RunResults runLesson(@RequestParam int courseId,
                                @RequestParam int lessonNumber) {
        try {

            RunResults results = runService.runLesson(courseId, lessonNumber);
            LOGGER.info("Run class (course ID: {}, lesson number: {}) - OK", courseId, lessonNumber);
            return results;

        } catch (Exception e) {

            LOGGER.error("Run class from lesson - FAILED", e);
            return new RunResults(new GeneralResponse(ResponseType.ERROR, e.getMessage()));
        }
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to run class from lesson with solution (need to add solution before)",
            notes = "Runs a class in certain lesson (class must have main method) which depends on Solution class",
            response = RunResults.class,
            produces = "application/json")
    @RequestMapping(value = "courses/lessons/send-solution-and-run", method = RequestMethod.POST)
    public RunResults runLessonWithSolution(@RequestParam int courseId,
                                            @RequestParam int lessonNumber,
                                            @RequestBody ExternalCode code) {
        try {

            RunResults results = runService.runLessonWithSolution(courseId, lessonNumber, code);
            LOGGER.info("Run class with solution (course ID: {}, lesson number: {}) - OK", courseId, lessonNumber);
            return results;

        } catch (Exception e) {

            LOGGER.error("Run class from lesson with solution - FAILED", e);
            return new RunResults(new GeneralResponse(ResponseType.ERROR, e.getMessage()));
        }
    }
}
