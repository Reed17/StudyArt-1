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
import ua.artcode.exceptions.*;
import ua.artcode.model.*;
import ua.artcode.utils.MailUtils;
import ua.artcode.utils.ResultChecker;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;

import static ua.artcode.enums.UserType.TEACHER;


@Service
public class UserServiceImpl implements UserService {

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

    private static Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(TeacherRepository teacherDB, StudentRepository studentDB, ValidationUtils validationUtils,
                           SecurityUtils securityUtils, SessionRepository sessionDB, MailUtils mu,
                           ResultChecker resultChecker, CourseRepository courseRepository) {
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
    public User activate(int userId) throws UnexpectedNullException {
        User user = teacherDB.findOne(userId);

        user = user == null ? studentDB.findOne(userId) : user;

        resultChecker.checkNull(user, "User with this id doesn't exists");

        user.activate();

        return user;
    }

    @Override
    public String login(String login, String pass) throws InvalidLoginInfo {

        User loginResult;

        boolean validatedPass = validationUtils.passValidation(pass);

        if (!validationUtils.emailValidation(login) && !validationUtils.loginValidation(login)) {
            LOGGER.error(String.format("Login %s has wrong format", login));
            throw new InvalidLoginInfo("Wrong login format");

        }
        if (validatedPass) {
            pass = securityUtils.encryptPass(pass);

            loginResult = teacherDB.findByEmail(login) == null ?
                    studentDB.findByEmail(login) : teacherDB.findByEmail(login);
            if (loginResult == null) {
                loginResult = teacherDB.findByLogin(login) == null ?
                        studentDB.findByLogin(login) : teacherDB.findByLogin(login);
            }

            if (loginResult != null && loginResult.getPass().equals(pass)) {
                Session session = sessionDB.findByUser(loginResult);

                if (session == null)
                    return sessionDB.save(new Session(loginResult)).getAccessKey();
                else
                    return session.getAccessKey();
            }
        }
        LOGGER.error(String.format("User with login %s doesn't exist", login));
        throw new InvalidLoginInfo("User doesn't exists");
    }

    @Override
    public User find(String accessKey) throws InvalidUserSessionException {
        Session session = sessionDB.findOne(accessKey);
        if (session == null) {
            throw new InvalidUserSessionException("Session not found.");
        }
        return session.getUser();
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
            if (teacher.getPass().equals(securityUtils.encryptPass(oldPass))) {
                teacher.setPass(securityUtils.encryptPass(newPass));
                teacher.setEmail(email.isEmpty() ? teacher.getEmail() : email);
                return teacherDB.save(teacher) != null;
            }
        } else {
            Student student = studentDB.findOne(userId);
            if (student.getPass().equals(securityUtils.encryptPass(oldPass))) {
                student.setPass(securityUtils.encryptPass(newPass));
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
