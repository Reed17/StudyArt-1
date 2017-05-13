package ua.artcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.artcode.dao.CourseDB;
import ua.artcode.dao.UserDB;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
import ua.artcode.model.*;
import ua.artcode.service.StudentService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by v21k on 18.04.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDB userDB;

    @Autowired
    private CourseDB courseDB;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ObjectMapper mapper;

    @Value("${GitURL}")
    private String GitURL;

    private static String tempPathForGitProjects;

    private static String tempPathForExternalCodeCompiling;

    // TODO create temp folders before all tests, then removeCourse them in the end (after all tests) ????????

    @Value("${pathForGitProjects}")
    public void setPathForGitProjects(String path) {
        tempPathForGitProjects = path;
    }

    @Value("${pathForExternalCodeCompiling}")
    public void setPathForExternalCodeCompiling(String path) {
        tempPathForExternalCodeCompiling = path;
    }

    @Test
    public void testAddPositive() throws Exception {
        addCourse();
    }

    // validation check
    @Test
    public void testAddNegative() throws Exception {
        Course course = new Course(0,
                "someCourse",
                "V",
                "https://github.com/v21k/fake-path/TestGitProject.gif",
                null,
                null);
        mockMvc.perform(post("/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void testGetPositive() throws Exception {
        addCourse();
        mockMvc.perform(get("/courses/get?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("someCourse"))
                .andExpect(jsonPath("$.author").value("VK"))
                .andExpect(jsonPath("$.url").value(GitURL));
    }

    @Test
    public void testGetNegative() throws Exception {
        try {
            mockMvc.perform(get("/courses/get?id=1"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getMessage(), containsString("No course found with id: 1"));
        }
    }

    @Test
    public void testRunClassPositive() throws Exception {
        ExternalCode code = new ExternalCode("public class test " +
                "{\npublic static void main(String[] args) " +
                "{\nSystem.out.print(2+2);\n}\n}\n");

        mockMvc.perform(post("/run-class")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(code))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.methodResult.systemOut").value("4"));
    }

    @Test
    public void testRunClassNegative() throws Exception {
        ExternalCode code = new ExternalCode("public class test " +
                "{\npublic static void main(String[] args) " +
                "{\nSystem.out.println(2+2;\n}\n}\n");
        mockMvc.perform(post("/run-class")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(code)))
                .andExpect(jsonPath("$.generalResponse.message").value(containsString("error")));
    }

    @Test
    public void testRunLessonPositive() throws Exception {
        addCourse();

        mockMvc.perform(get("/courses/lessons/run?courseId=1&lessonNumber=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.methodResult.systemOut").value("SOME INFO\n"));
    }

    @Test
    public void testRunLessonNegative() throws Exception {
        addCourse();

        mockMvc.perform(get("/courses/lessons/run?courseId=1&lessonNumber=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("error")));
    }

    @Test
    public void testRunLessonWithSolutionPositive() throws Exception {
        addCourse();
        ExternalCode code = new ExternalCode("public static int sum(int a, int b){return a+b;}");

        mockMvc.perform(post("/courses/lessons/send-solution-and-run?courseId=1&lessonNumber=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(code)))
                .andExpect(status().isOk())
                .andExpect(content().string(is(not(containsString("error")))))
                .andExpect(content().string(is(containsString("7"))))
                .andExpect(content().string(is(containsString("9"))));
    }

    @Test
    public void testRunLessonWithSolutionNegative() throws Exception {
        addCourse();
        ExternalCode code = new ExternalCode("public static int sum(int a, int b){return a+;}");

        mockMvc.perform(post("/courses/lessons/send-solution-and-run?courseId=1&lessonNumber=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(code)))
                .andExpect(status().isOk())
                .andExpect(content().string(is(containsString("error"))));
    }

    @Test
    public void getUserCourseStatistic()
            throws Exception,
            InvalidUserLoginException,
            InvalidUserEmailException,
            InvalidUserPassException {
        List<Course> courses = new ArrayList<>();
        Course e = new Course(0,
                "someCourse",
                "VK",
                GitURL,
                null,
                null);
        courses.add(e);
        courseDB.addCourse(e);
        studentService.register("login1", "123456", "newbie@gmail.com");

        // todo use Repositories
        Student found = (Student) userDB.getUserByLogin("login1");
        studentService.activate(found.getId());
        found.subscribeTo(e);
        found.setCompleted(courses);
        String actual = studentService.getUserCourseStatistic(found.getLogin(), e.getId());
        // todo extract to separate file or ???
        String expected = "{\"value\":" +
                "{\"id\":4,\"name\":\"someCourse\",\"author\":\"VK\"," +
                "\"url\":\"https://github.com/v21k/TestGitProject.git\"," +
                "\"localPath\":\"course/4someCourse/\"," +
                "\"lessons\":[{\"id\":0,\"name\":\"_01_lesson\",\"localPath\":\"course/4someCourse/src/main/java/_01_lesson\"}," +
                "{\"id\":0,\"name\":\"_02_lesson\",\"localPath\":\"course/4someCourse/src/main/java/_02_lesson\"}," +
                "{\"id\":0,\"name\":\"_03_lesson\",\"localPath\":\"course/4someCourse/src/main/java/_03_lesson\"}," +
                "{\"id\":0,\"name\":\"_04_lesson\",\"localPath\":\"course/4someCourse/src/main/java/_04_lesson\"}]}}";
        // todo use lib to compare two jsons. Spring test knows how to do it. Assertions within jsons. JSON path
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    private void addCourse() throws Exception {
        Course course = new Course(0,
                "someCourse",
                "VK",
                GitURL,
                null,
                null);
        mockMvc.perform(post("/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @AfterClass
    public static void removeTempDir() throws IOException {
        File externalCodeCompiling = new File(tempPathForExternalCodeCompiling);
        if (externalCodeCompiling.exists() && externalCodeCompiling.isDirectory())
            FileUtils.deleteDirectory(externalCodeCompiling);
        File gitProjects = new File(tempPathForGitProjects);
        if (gitProjects.exists() && gitProjects.isDirectory())
            FileUtils.deleteDirectory(gitProjects);
    }
}
