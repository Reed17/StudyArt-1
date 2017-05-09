package ua.artcode.service;

import ua.artcode.exceptions.CourseNotFoundException;
import ua.artcode.exceptions.InvalidIDException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.model.Student;

import javax.security.auth.login.LoginException;

/**
 * Created by reed on 30.04.17.
 */
public interface IStudentService extends UserService<Student> {

    String getUserCourseStatistic(String login, int courseID) throws InvalidIDException, CourseNotFoundException, LoginException, InvalidUserLoginException;
}
