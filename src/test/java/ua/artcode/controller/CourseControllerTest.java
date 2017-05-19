package ua.artcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.LessonRepository;
import ua.artcode.model.Course;
import ua.artcode.model.CourseFromUser;
import ua.artcode.model.ExternalCode;
import ua.artcode.model.Lesson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseControllerTest {

    private static String tempPathForGitProjects;
    private static String tempPathForExternalCodeCompiling;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Value("${test.git.URL}")
    private String gitURL;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;



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

    @Test
    public void testAddPositive() throws Exception {
        addCourse();
    }

    @Test
    public void testAddLesson() throws Exception {
        addCourse();
        addLesson();
        mockMvc.perform(get("/courses/lessons/get?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("first_lesson"))
                .andExpect(jsonPath("$.courseID").value(1))
                .andExpect(jsonPath("$.sourcesRoot").value("courses"+File.separator+"1someCourse"+
                        File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"_02_lesson"))
                .andExpect(jsonPath("$.description").value("First test lesson"));
    }

    // validation check
    @Test
    public void testAddNegative() throws Exception {
        Course course = new Course();
        course.setName("someCourse");
        course.setAuthor("Maks");
        course.setUrl("https://github.com/v21k/fake-path/TestGitProject.gif");
        course.setDescription("Just test cource");
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
                .andExpect(jsonPath("$.url").value(gitURL));
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
        addLesson();

        CourseFromUser courseFromUser = new CourseFromUser();
        courseFromUser.setName("someCourse");
        courseFromUser.setUrl(gitURL);
        courseFromUser.setId(1);

        mockMvc.perform(post("/courses/lessons/send-solution-and-run-tests?courseId=1&lessonNumber=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(courseFromUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.methodResult.systemOut").value(""))
                .andExpect(jsonPath("$.methodStats.passedTests").value(1));
    }

    @Test
    public void testRunLessonNegative() throws Exception {
        addCourse();

        CourseFromUser courseFromUser = new CourseFromUser();
        courseFromUser.setName("someCourse");
        courseFromUser.setUrl(gitURL);
        courseFromUser.setId(1);

        mockMvc.perform(post("/courses/lessons/send-solution-and-run-tests?courseId=1&lessonNumber=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(courseFromUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.generalResponse.type").value("ERROR"));
    }

    private void addCourse() throws Exception {

        Course course = new Course();
        String name = "someCourse";
        course.setName(name);
        course.setAuthor("VK");
        course.setUrl(gitURL);
        course.setDescription("Just test cource");
        course.setSourcesRoot("src/main/java".replace("/", File.separator));
        course.setTestsRoot("src/test/java".replace("/", File.separator));
        mockMvc.perform(post("/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void addLesson() throws Exception {

        List<String> testClassPaths = new ArrayList<>(1);
        testClassPaths.add("src/test/java/_02_lesson/SolutionTests.java");

        Lesson lesson = new Lesson();
        lesson.setName("first_lesson");
        lesson.setDescription("First test lesson");
        lesson.setCourseID(1);
        lesson.setSourcesRoot("src/main/java/_02_lesson");
        lesson.setTestsClasses(testClassPaths);
        lesson.setLocalPath("_02_lesson");

        mockMvc.perform(post("/courses/lessons/add?courseId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(lesson))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}