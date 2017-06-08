package ua.artcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.controller.CourseController;
import ua.artcode.controller.UserController;
import ua.artcode.enums.UserType;
import ua.artcode.exceptions.SuchCourseAlreadyExists;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.model.dto.RegisterRequestDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by v21k on 08.06.17.
 */
@Component
public class CommandLineRunnerUtils {

    private final UserController userController;
    private final CourseController courseController;

    @Autowired
    public CommandLineRunnerUtils(UserController userController, CourseController courseController) {
        this.userController = userController;
        this.courseController = courseController;
    }

    public void registerTestUsers() {
        try {
            userController.registerUser(
                    new RegisterRequestDTO("student",
                            "student@gmail.com",
                            "password",
                            UserType.STUDENT));
            userController.registerUser(
                    new RegisterRequestDTO("student2",
                            "student2@gmail.com",
                            "password",
                            UserType.STUDENT));
            userController.registerUser(
                    new RegisterRequestDTO("teacher",
                            "teacher@gmail.com",
                            "password",
                            UserType.TEACHER));
        } catch (Throwable ignored) {
        }
    }

    public void createCoursesAndLessons() throws SuchCourseAlreadyExists {
        Map<Integer, String> courseName = new HashMap<>();
        Map<Integer, String> courseDescription = new HashMap<>();

        courseDescription.put(1, "Reflection API");
        courseDescription.put(2, "JDBC, Drivers, SQL");
        courseDescription.put(3, "Servlets, servlet containers, JSP");
        courseDescription.put(4, "REST/SOAP");
        courseDescription.put(5, "JPA, Hibernate");

        courseName.put(1, "Reflection API");
        courseName.put(2, "JDBC + SQL");
        courseName.put(3, "Java Servlet API");
        courseName.put(4, "Web Services");
        courseName.put(5, "Java JPA");


        for (int i = 1; i <= courseName.size(); i++) {


            courseController.addCourse(new Course(
                    courseName.get(i),
                    "Vlad Kornieiev",
                    "https://github.com/v21k/TestGitProject.git",
                    courseDescription.get(i),
                    "src/main/java",
                    "src/test/java"), null);

            for (int j = 0; j < 3; j++) {
                courseController.addLessonToCourse(i, new Lesson(
                                "Test lesson " + j,
                                "_02_lesson",
                                null,
                                null,
                                Collections.singletonList("src/test/java/_02_lesson/SolutionTests.java"),
                                "src/main/java/_02_lesson",
                                null,
                                "Test description"),
                        null
                );
            }
        }
    }

}
