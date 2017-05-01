package ua.artcode.utils;

import org.junit.Before;
import org.junit.Test;
import ua.artcode.dao.UserDB;
import ua.artcode.dao.UserDBImpl;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.model.Student;
import ua.artcode.model.Teacher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by zhenia on 28.04.17.
 */
public class ValidationUtilsTest {

    private ValidationUtils validationUtils = new ValidationUtils();
    private TeacherRepository teachers;
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
