package ua.artcode.utils;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.controller.CourseController;
import ua.artcode.controller.UserController;
import ua.artcode.enums.UserType;
import ua.artcode.exceptions.AppException;
import ua.artcode.exceptions.SuchCourseAlreadyExists;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.model.dto.RegisterRequestDTO;
import ua.artcode.service.CourseService;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.Arrays;
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
    private final CourseService courseService;

    @Autowired
    public CommandLineRunnerUtils(UserController userController, CourseController courseController, CourseService courseService) {
        this.userController = userController;
        this.courseController = courseController;
        this.courseService = courseService;
    }

    public void registerTestUsers() {
        try {
            userController.registerUser(
                    new RegisterRequestDTO("student",
                            "student@gmail.com",
                            "password",
                            UserType.STUDENT));
            userController.registerUser(
                    new RegisterRequestDTO("student",
                            "student@gmail.com",
                            "password",
                            UserType.STUDENT));
            userController.registerUser(
                    new RegisterRequestDTO("teacher44",
                            "teacher@gmail.com",
                            "password",
                            UserType.TEACHER));
        } catch (Throwable ignored) {
        }
    }


    // todo Why did you use Contoroller for adding, you might use courseService
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

        String theory = "### Understanding Java’s Reflection API in Five Minutes\n" +
                "A language that is capable of **reflection**, like **Java** is, allows developers to inspect _types, methods, fields, annotations, etc._" +
                " at run time and to defer the decision on how to use them from compile time to run time. To that end, Java’s **reflection API** offers types like " +
                "Class, Field, Constructor, Method, Annotation, and others. With them it is possible to interact with types that were not known at compile time, " +
                "for example to create instances of an unknown class and call methods on them.\n" +
                "\n" +
                "This quick tip is intended to give you a high-level understanding of what **reflection** is, what it looks like " +
                "in Java, and what it could be used for. After it you will be ready to get started or work through longer tutorials. " +
                "To get the most out of it you should have a good understanding of how Java is structured, specifically what classes " +
                "and methods are and how they relate. Knowing about annotations unlocks a separate section.";
        String description = "This lesson is dedicated to _Java Reflection API_.  Please do\n * Task1\n * Task2\n * Task 3 ";

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
                                Collections.singletonList("src/test/java/_02_lesson/SolutionTests.java"),
                                "src/main/java/_02_lesson",
                                description,
                                theory),
                        null
                );
            }
        }
    }

    public void createBaseCourse() throws SuchCourseAlreadyExists {
        String courseName = "ArtCodeBaseCourse";
        String description = "BaseCourseDesc";


        Course savedCourse = courseService.addCourse(new Course(
                courseName,
                "Serhii Bilobrov",
                "https://github.com/ksyashka/ACBCourse.git",
                description,
                "src/main/java",
                "src/test/java"));


        // how to create lesson correctly?
        Lesson firstLesson = new Lesson(
                "FirstLesson",
                "week1",
                Collections.singletonList("Task1.java"),
                Collections.singletonList("TestTask1.java"), // todo if null - parse from local path
                "src/main/java/week1", "src/test/java/week1", ""); // remove
        // todo we already have 'src/main/java' and 'course local path', so there is no need in sources/tests root

        try {
            Lesson savedLesson = courseService.addLesson(firstLesson, savedCourse.getId());
        } catch (GitAPIException | IOException | ValidationException | AppException e) {
            e.printStackTrace();
        }


    }

}
