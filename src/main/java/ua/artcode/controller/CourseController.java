package ua.artcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.CheckResult;
import ua.artcode.model.Course;
import ua.artcode.model.GeneralResponse;
import ua.artcode.model.SolutionModel;
import ua.artcode.service.CourseService;

import java.io.IOException;
import java.util.Collection;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("/courses/get")
    public Course getCourse(@RequestParam int id) {
        Course course = null;
        try {
            course = courseService.getCourse(id);
        } catch (CourseNotFoundException e) {
            e.printStackTrace();
        }
        return course;
    }

    @RequestMapping("courses/getAll")
    public Collection<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @RequestMapping(path = {"/courses/add"}, method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody Course course) throws IOException {
        return courseService.addCourse(course) ? GeneralResponse.DONE : GeneralResponse.FAILED;
    }

    @RequestMapping(path = {"/run-task"})
    public CheckResult runTask(@RequestParam String mainClass, @RequestParam int courseId) {
        return courseService.runTask(mainClass, courseId);
    }

    @RequestMapping(value = "/send-solution", method = RequestMethod.POST)
    public CheckResult sendSolution(@RequestBody SolutionModel solution, @RequestParam String mainClass, @RequestParam int courseId) {
        return courseService.checkSolution(mainClass, courseId, solution);
    }
}
