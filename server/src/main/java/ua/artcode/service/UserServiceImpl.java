package ua.artcode.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.SessionRepository;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.enums.UserType;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
import ua.artcode.exceptions.UserNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.Student;
import ua.artcode.model.Teacher;
import ua.artcode.model.User;
import ua.artcode.utils.MailUtils;
import ua.artcode.utils.ResultChecker;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;

import static ua.artcode.enums.UserType.TEACHER;


@Service
public class UserServiceImpl implements UserService {

    private static Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private final TeacherRepository teacherDB;
    private final StudentRepository studentDB;
    private final ValidationUtils validationUtils;
    private final SecurityUtils securityUtils;
    private final SessionRepository sessionDB;
    private final MailUtils mu;
    private final ResultChecker resultChecker;
    private final CourseRepository courseRepository;
    @Value("${email.user}")
    private String emailUsername;

    @Autowired
    public UserServiceImpl(TeacherRepository teacherDB, StudentRepository studentDB, ValidationUtils validationUtils, SecurityUtils securityUtils, SessionRepository sessionDB, MailUtils mu, ResultChecker resultChecker, CourseRepository courseRepository) {
        this.teacherDB = teacherDB;
        this.studentDB = studentDB;
        this.validationUtils = validationUtils;
        this.securityUtils = securityUtils;
        this.sessionDB = sessionDB;
        this.mu = mu;
        this.resultChecker = resultChecker;
        this.courseRepository = courseRepository;
    }


    @Override
    public User register(String login, String pass, String email, UserType type)
            throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException {
        User newUser;

        LOGGER.debug(String.format("Registration params: %s, %s, %s, %s", login, pass, email, type));

        // checking is parameters valid
        validationUtils.validateAllUserFields(login, email, pass);

        // checking does user with this parameters already exist
        validationUtils.checkOriginality(login, email, teacherDB, studentDB);

        if (TEACHER.equals(type))
            newUser = teacherDB.save(new Teacher(login, securityUtils.encryptPass(pass), email));
        else
            newUser = studentDB.save(new Student(login, securityUtils.encryptPass(pass), email));

        mu.sendEmail(emailUsername, newUser.getEmail(), "Registration", mu.getActivationLink(newUser));

        return newUser;
    }

    @Override
    public User activate(int userId) throws UserNotFoundException {
        User user = teacherDB.findOne(userId);

        user = user == null ? studentDB.findOne(userId) : user;

        if (user == null) {
            String message = "User with this id doesn't exists";
            UserNotFoundException userNotFoundException = new UserNotFoundException(message);

            LOGGER.error(message, userNotFoundException);
            throw userNotFoundException;
        }

        user.activate();

        return user;
    }

    @Override
    public User findByUserName(String userName) throws InvalidUserLoginException {
        Teacher teacher = teacherDB.findByUsername(userName);
        Student student = studentDB.findByUsername(userName);
        if (student == null && teacher == null) {
            throw new InvalidUserLoginException("User with name " + userName + " not found.");
        }
        return teacher != null ? teacher : student;
    }

    @Override
    public boolean subscribe(int courseId, int userId) {
        Student student = studentDB.findOne(userId);
        Course course = courseRepository.findOne(courseId);

        return student.subscribeTo(course) && studentDB.save(student) != null;
    }

    @Override
    public boolean changePersonalInfo(String oldPass, String newPass, String email, int userId, UserType userType) {
        if (userType.equals(TEACHER)) {
            Teacher teacher = teacherDB.findOne(userId);
            if (teacher.getPassword().equals(securityUtils.encryptPass(oldPass))) {
                teacher.setPassword(securityUtils.encryptPass(newPass));
                teacher.setEmail(email.isEmpty() ? teacher.getEmail() : email);
                return teacherDB.save(teacher) != null;
            }
        } else {
            Student student = studentDB.findOne(userId);
            if (student.getPassword().equals(securityUtils.encryptPass(oldPass))) {
                student.setPassword(securityUtils.encryptPass(newPass));
                student.setEmail(email.isEmpty() ? student.getEmail() : email);
                return studentDB.save(student) != null;
            }
        }
        return false;
    }

    @Override
    public void deleteAccount(int userId) {
        // todo Referential integrity constraint violation
        // todo PUBLIC.STUDENT_SUBSCRIBED FOREIGN KEY(COURSE_ID) REFERENCES PUBLIC.COURSES(COURSE_ID)
        Teacher teacher = teacherDB.findOne(userId);

        if (teacher != null) {
            teacherDB.delete(userId);
        } else {
            studentDB.delete(userId);
        }
    }
}
