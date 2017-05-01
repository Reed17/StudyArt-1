package ua.artcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import ua.artcode.dao.UserDB;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.dao.repositories.UserRepository;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
import ua.artcode.model.Student;
import ua.artcode.model.Teacher;
import ua.artcode.model.User;
import ua.artcode.utils.MailUtils;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhenia on 24.04.17.
 */
@Service
public class StudentService implements UserService<Student> {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;


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
        validationUtils.checkOriginality(login, email, teacherRepository, studentRepository);


        Student newStudent = studentRepository.save(new Student(login, securityUtils.encryptPass(pass), email));

        ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Mail.xml");
        MailUtils mu = (MailUtils) context.getBean("mailUtils");
        mu.sendEmail("${emailUsername}", newStudent.getEmail(), "Registration", mu.getActivationLink(newStudent));

        return newStudent;
    }

    @Override
    public Student activate(int userId) {
        Student student = studentRepository.findOne(userId);

        if(student != null) student.activate();

        return student;
    }
}
