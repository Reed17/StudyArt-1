package ua.artcode.dao.repositories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.artcode.Application;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
public class TestCourseRepositories {
    private Course course1;

    @Value("${GitURL}")
    private String GitURL;
    @Autowired
    private CourseRepository repository;

    @Before
    public void beforeTests() {
        List<Lesson> lessons1 = new ArrayList<>();
        lessons1.add(new Lesson("MyLesson", "lessonPath"));
        lessons1.add(new Lesson("YourLesson", "lessonpath"));
        course1 = new Course("name", "author", GitURL, "coursePath", lessons1);
        repository.save(course1);
    }

    @Test
    public void testSaveCourse() {
        assertThat(1, is(repository.count()));
    }

    @Test
    public void findCourse() {
        assertThat(course1, is(repository.findOne(1)));
    }

    @Test
    public void existCourse() {
        assertTrue(repository.exists(1));
    }


}
