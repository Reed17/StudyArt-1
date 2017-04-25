package ua.artcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.artcode.dao.UserDB;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
import ua.artcode.model.Student;
import ua.artcode.model.Teacher;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;

/**
 * Created by zhenia on 24.04.17.
 */
public class StudentService implements UserService<Student> {

    @Autowired
    private UserDB<Teacher> teacherDB;

    @Autowired
    private UserDB<Student> studentDB;

    @Autowired
    private ValidationUtils validationUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public Student register(String login, String pass, String email)
            throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException {

        // checking is parameters valid
        validationUtils.validateAllUserFields(login, email, pass);


        // checking does user with this parameters already exist
        validationUtils.checkOriginality(login, email, teacherDB, studentDB);


        Student newStudent = studentDB.add(new Student(login, securityUtils.encryptPass(pass), email));

        // TODO email sending

        return newStudent;
    }

    @Override
    public Student activate(int userId) {
        Student student = studentDB.getUserById(userId);

        if(student != null) student.activate();

        return student;
    }
}
