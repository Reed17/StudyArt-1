package ua.artcode.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.dao.StudyDB;
import ua.artcode.exceptions.DirectoryCreatingException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.LessonsParsingException;
import ua.artcode.model.Course;
import ua.artcode.model.GeneralResponse;

@RestController
public class CourseController {

    @Autowired
    private StudyDB<Course> courseDB;

    @RequestMapping("/сourses/get")
    public Course getCourseByID(@RequestParam int id) throws InvalidIDException {
        return courseDB.getByID(id);
    }

    @RequestMapping(value = "/courses/add", method = RequestMethod.POST)
    public GeneralResponse add(@RequestBody Course course) throws DirectoryCreatingException, LessonsParsingException, GitAPIException {
        return courseDB.add(course) ? new GeneralResponse("OK") : new GeneralResponse("FAIL");
    }
}
