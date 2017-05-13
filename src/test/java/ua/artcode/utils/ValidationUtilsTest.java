package ua.artcode.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.artcode.Application;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.model.Student;
import ua.artcode.model.Teacher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by zhenia on 28.04.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
public class ValidationUtilsTest {

    @Autowired
    private ValidationUtils validationUtils;
    @Autowired
    private TeacherRepository teachers;
    @Autowired
    private StudentRepository students;

    @Before
    public void setUp() {
        validationUtils = new ValidationUtils();

        teachers.save(new Teacher("teacher", "password", "teacher@gmail.com"));
        students.save(new Student("student", "password", "student@gmail.com"));
    }

    @Test
    public void loginValidationPositiveTest() {
        assertThat(validationUtils.loginValidation("username"), is(true));
        assertThat(validationUtils.loginValidation("USERNAME"), is(true));
        assertThat(validationUtils.loginValidation("username123"), is(true));
    }

    @Test
    public void loginValidationNegativeTest() {
        assertThat(validationUtils.loginValidation("user"), is(false));
        assertThat(validationUtils.loginValidation("?USERNAME"), is(false));
        assertThat(validationUtils.loginValidation("too_long_username"), is(false));
    }

    @Test
    public void passValidationPositiveTest() {
        assertThat(validationUtils.passValidation("password"), is(true));
        assertThat(validationUtils.passValidation("PASSWORD"), is(true));
        assertThat(validationUtils.passValidation("Password_123"), is(true));
    }

    @Test
    public void passValidationNegativeTest() {
        assertThat(validationUtils.passValidation("pass"), is(false));
        assertThat(validationUtils.passValidation("?password"), is(false));
        assertThat(validationUtils.passValidation("too_long_password"), is(false));
    }

    @Test
    public void emailValidationPositiveTest() {
        assertThat(validationUtils.emailValidation("testMail@gmail.com"), is(true));
    }

    @Test
    public void emailValidationNegativeTest() {
        assertThat(validationUtils.emailValidation("test.gmail.com"), is(false));
        assertThat(validationUtils.emailValidation("testMail@"), is(false));
    }

    @Test
    public void checkLoginOriginalityTest() {
        assertThat(validationUtils.checkLoginOriginality("teacher", teachers, students), is(false));
        assertThat(validationUtils.checkLoginOriginality("teacher1", teachers, students), is(true));
        assertThat(validationUtils.checkLoginOriginality("student", teachers, students), is(false));
    }

    @Test
    public void checkEmailOriginalityTest() {
        assertThat(validationUtils.checkEmailOriginality("teacher@gmail.com", teachers, students), is(false));
        assertThat(validationUtils.checkEmailOriginality("teacher1@gmail.com", teachers, students), is(true));
        assertThat(validationUtils.checkEmailOriginality("student@gmail.com", teachers, students), is(false));
    }
}
