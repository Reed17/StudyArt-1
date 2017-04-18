package ua.artcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;

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
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void testAddPositive() throws Exception {
        Course course = new Course(0,
                "someCourse",
                "VK",
                "https://github.com/v21k/TestGitProject.git",
                null,
                null);
        mockMvc.perform(post("/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
                .andExpect(jsonPath("$.url").value("https://github.com/v21k/TestGitProject.git"));
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
                "{\nSystem.out.println(2+2);\n}\n}\n");

        mockMvc.perform(post("/run-class")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(code))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outputInfo").value("4\n"));
    }

    @Test
    public void testRunClassNegative() throws Exception {
        ExternalCode code = new ExternalCode("public class test " +
                "{\npublic static void main(String[] args) " +
                "{\nSystem.out.println(2+2;\n}\n}\n");

        mockMvc.perform(post("/run-class")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(code)))
                .andExpect(jsonPath("$.outputInfo").value(not(4)));
    }

    @Test
    public void testRunLessonPositive() throws Exception {
        addCourse();

        mockMvc.perform(get("/courses/lessons/run?courseId=1&lessonNumber=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outputInfo").value("SOME INFO\n"));
    }

    @Test
    public void testRunLessonNegative() throws Exception {
        addCourse();

        mockMvc.perform(get("/courses/lessons/run?courseId=1&lessonNumber=2"))
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

    private void addCourse() throws Exception {
        Course course = new Course(0,
                "someCourse",
                "VK",
                "https://github.com/v21k/TestGitProject.git",
                null,
                null);
        mockMvc.perform(post("/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON));
    }
}
