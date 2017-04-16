package ua.artcode.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonsParsingException;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.GeneralResponse;
import ua.artcode.model.RunResults;
import ua.artcode.service.CourseService;
import ua.artcode.service.RunService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private RunService runService;

    @RequestMapping(value = "/courses/get", method = RequestMethod.GET)
    public Course getCourseByID(@RequestParam int id) throws InvalidIDException, CourseNotFoundException {
        return courseService.getByID(id);
    }

    @RequestMapping(value = "/courses/add", method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody Course course) {
        try {
            return courseService.addCourse(course) ? new GeneralResponse("OK") : new GeneralResponse("FAIL");
        } catch (GitAPIException | DirectoryCreatingException | LessonsParsingException e) {
            e.printStackTrace();
            return new GeneralResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/run-class", method = RequestMethod.POST)
    public RunResults runClass(@RequestBody ExternalCode code) throws ClassNotFoundException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return runService.runMain(code);
    }
}
