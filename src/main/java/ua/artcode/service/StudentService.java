package ua.artcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
import ua.artcode.model.Student;
import ua.artcode.utils.MailUtils;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;

/**
 * Created by zhenia on 24.04.17.
 */
@Service
public class StudentService implements UserService<Student> {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ValidationUtils validationUtils;
    private final SecurityUtils securityUtils;
    private final MailUtils mailUtils;

    @Autowired
    public StudentService(TeacherRepository teacherRepository, StudentRepository studentRepository, ValidationUtils validationUtils, SecurityUtils securityUtils, MailUtils mailUtils) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.validationUtils = validationUtils;
        this.securityUtils = securityUtils;
        this.mailUtils = mailUtils;
    }

    @Override
    public Student register(String login, String pass, String email)
            throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException {

        // checking is parameters valid
        validationUtils.validateAllUserFields(login, email, pass);


        // checking does user with this parameters already exist
        validationUtils.checkOriginality(login, email, teacherRepository, studentRepository);


        Student newStudent = studentRepository.save(new Student(login, securityUtils.encryptPass(pass), email));

        // todo send email

        return newStudent;
    }

    @Override
    public Student activate(int userId) {
        Student student = studentRepository.findOne(userId);

        if (student != null) student.activate();

        return student;
    }
}
