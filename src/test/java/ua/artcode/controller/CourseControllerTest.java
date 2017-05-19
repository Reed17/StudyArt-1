package ua.artcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.artcode.model.Course;
import ua.artcode.model.ExternalCode;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
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

    private static String tempPathForGitProjects;
    private static String tempPathForExternalCodeCompiling;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Value("${test.git.URL}")
    private String GitURL;

    private String teacherKey;
    private String studentKey;

    @AfterClass
    public static void removeTempDir() throws IOException {
        File externalCodeCompiling = new File(tempPathForExternalCodeCompiling);
        if (externalCodeCompiling.exists() && externalCodeCompiling.isDirectory())
            FileUtils.deleteDirectory(externalCodeCompiling);
        File gitProjects = new File(tempPathForGitProjects);
        if (gitProjects.exists() && gitProjects.isDirectory())
            FileUtils.deleteDirectory(gitProjects);
    }

    @Value("${application.courses.paths.git}")
    public void setPathForGitProjects(String path) {
        tempPathForGitProjects = path;
    }

    @Value("${application.courses.paths.externalCode}")
    public void setPathForExternalCodeCompiling(String path) {
        tempPathForExternalCodeCompiling = path;
    }

    @Before
    public void registerTestUsers() throws Exception{
        mockMvc.perform(post("/register?login=Username1&email=42004200zhenia@gmail.com&pass=password1&type=teacher"));
        mockMvc.perform(post("/register?login=Username2&email=zheniatrochun@ukr.net&pass=password1&type=student"));

        teacherKey = mockMvc.perform(post("/login?login=Username1&pass=password1")).andReturn().getResponse().getContentAsString();
        studentKey = mockMvc.perform(post("/login?login=Username2&pass=password1")).andReturn().getResponse().getContentAsString();
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
                .cookie(new Cookie("Access-Key", teacherKey))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void testGetPositive() throws Exception {
        addCourse();
        mockMvc.perform(get("/courses/get?id=1")
                .cookie(new Cookie("Access-Key", teacherKey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("someCourse"))
                .andExpect(jsonPath("$.author").value("VK"))
                .andExpect(jsonPath("$.url").value(GitURL));
    }

    @Test
    public void testGetNegative() throws Exception {
        try {
            mockMvc.perform(get("/courses/get?id=1")
                    .cookie(new Cookie("Access-Key", teacherKey)));
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
                .cookie(new Cookie("Access-Key", studentKey))
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
                .cookie(new Cookie("Access-Key", teacherKey))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(code)))
                .andExpect(jsonPath("$.generalResponse.message").value(containsString("error")));
    }

    @Test
    public void testRunLessonPositive() throws Exception {
        addCourse();

        mockMvc.perform(get("/courses/lessons/run?courseId=1&lessonNumber=3")
                .cookie(new Cookie("Access-Key", teacherKey)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.methodResult.systemOut").value("SOME INFO" + System.getProperty("line.separator")));
    }

    @Test
    public void testRunLessonNegative() throws Exception {
        addCourse();

        mockMvc.perform(get("/courses/lessons/run?courseId=1&lessonNumber=1")
                .cookie(new Cookie("Access-Key", teacherKey)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ERROR")));
    }

    private void addCourse() throws Exception {
        Course course = new Course(0,
                "someCourse",
                "VK",
                GitURL,
                null,
                null);
        mockMvc.perform(post("/courses/add")
                .cookie(new Cookie("Access-Key", teacherKey))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}