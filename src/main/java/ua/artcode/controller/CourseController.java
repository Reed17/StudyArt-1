package ua.artcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.GeneralResponse;
import ua.artcode.service.CourseService;

import java.io.IOException;

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

    @RequestMapping(path = {"/course/add"}, method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody Course course) throws IOException {
        return courseService.addCourse(course) ? GeneralResponse.DONE : GeneralResponse.FAILED;
    }

    @RequestMapping(path = {"/run-task"})
    public GeneralResponse runTask(@RequestParam String mainClass, @RequestParam int courseId) {
        return courseService.runTask("Test", courseId) ? GeneralResponse.DONE : GeneralResponse.FAILED;
    }
}
