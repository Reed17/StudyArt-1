package ua.artcode.service;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.CourseRepository;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.enums.UserType;
import ua.artcode.exceptions.*;
import ua.artcode.model.*;
import ua.artcode.utils.AppPropertyHolder;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.MailUtils;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;

import java.util.*;

import static ua.artcode.enums.UserType.TEACHER;


@Service
public class UserServiceImpl implements UserService {

    private static Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private final TeacherRepository teacherDB;
    private final StudentRepository studentDB;
    private final ValidationUtils validationUtils;
    private final SecurityUtils securityUtils;
    private final MailUtils mu;
    private final CourseRepository courseRepository;
    private final AppPropertyHolder.Email mailProps;
    private final CourseIOUtils courseIOUtils;

    @Autowired
    public UserServiceImpl(TeacherRepository teacherDB,
                           StudentRepository studentDB,
                           ValidationUtils validationUtils,
                           SecurityUtils securityUtils,
                           MailUtils mu,
                           CourseRepository courseRepository,
                           AppPropertyHolder appPropertyHolder,
                           CourseIOUtils courseIOUtils) {
        this.teacherDB = teacherDB;
        this.studentDB = studentDB;
        this.validationUtils = validationUtils;
        this.securityUtils = securityUtils;
        this.mu = mu;
        this.courseRepository = courseRepository;
        this.mailProps = appPropertyHolder.getEmail();
        this.courseIOUtils = courseIOUtils;
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

        mu.sendEmail(mailProps.getUser(), newUser.getEmail(), "Registration", mu.getActivationLink(newUser));

        return newUser;
    }


    // todo ask about this
    @Override
    public boolean activate(int userId) throws UserNotFoundException {
        User user = teacherDB.findOne(userId);

        if (Objects.isNull(user)) {
            user = studentDB.findOne(userId);
        }

        if (Objects.isNull(user)) {

            throw new UserNotFoundException("User with this id doesn't exists");
        }

        user.activate();

        // todo try avoid casting!
        if (user.getUserType() == UserType.TEACHER) {
            teacherDB.save((Teacher) user);
        } else {
            studentDB.save((Student) user);
        }

        return user.isActivated();
    }

    @Override
    public User findByUserName(String userName) throws InvalidUserLoginException {

        User user = teacherDB.findByUsername(userName);

        if (Objects.isNull(user)) {
            user = studentDB.findByUsername(userName);
        }

        if (Objects.isNull(user)) {

            throw new InvalidUserLoginException("User with name \"" + userName + "\" not found.");
        }

        return user;
    }

    @Override
    public boolean subscribe(int courseId, int userId)
            throws GitAPIException, DirectoryCreatingException, CourseNotFoundException {

        Student student = studentDB.findOne(userId);
        Course course = courseRepository.findOne(courseId);

        if (course == null) {
            throw new CourseNotFoundException("Course with id \"" + courseId + "\" does not exists");
        }

        Set<Course> subscribed = student.getSubscribed();

        if (!subscribed.contains(course) && subscribed.add(course)) {
            Map<Integer, UserCourseCopy> copies = student.getUserCourseCopies();

            int prevSize = copies.size();

            copies.put(courseId,
                            new UserCourseCopy(
                                    courseIOUtils.saveCourseLocally(
                                            course.getUrl(), course.getName(), course.getId(), userId)));

            return (studentDB.save(student) != null) && (copies.size() > prevSize);
        }

        return false;
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
