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
 * Created by zhenia on 23.04.17.
 */
public class TeacherService implements UserService<Teacher> {

    @Autowired
    private UserDB<Teacher> teacherDB;

    @Autowired
    private UserDB<Student> studentDB;

    @Autowired
    private ValidationUtils validationUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public Teacher register(String login, String pass, String email) throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException {

        // checking is parameters valid
        validationUtils.validateAllUserFields(login, email, pass);


        // checking does user with this parameters already exist
        validationUtils.checkOriginality(login, email, teacherDB, studentDB);
        

        Teacher newTeacher = teacherDB.add(new Teacher(login, securityUtils.encryptPass(pass), email));

        // TODO email sending

        return newTeacher;
    }

    @Override
    public Teacher activate(int userId) {
        Teacher teacher = teacherDB.getUserById(userId);

        teacher.activate();

        return teacher;
    }
}
