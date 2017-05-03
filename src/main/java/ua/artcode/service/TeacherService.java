package ua.artcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.repositories.StudentRepository;
import ua.artcode.dao.repositories.TeacherRepository;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
import ua.artcode.model.Teacher;
import ua.artcode.utils.SecurityUtils;
import ua.artcode.utils.ValidationUtils;



/**
 * Created by zhenia on 23.04.17.
 */
@Service
public class TeacherService implements UserService<Teacher> {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ValidationUtils validationUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public Teacher register(String login, String pass, String email) throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException {

        // checking is parameters valid
        validationUtils.validateAllUserFields(login, email, pass);


        validationUtils.checkOriginality(login, email, teacherRepository, studentRepository);


        Teacher newTeacher = teacherRepository.save(new Teacher(login, securityUtils.encryptPass(pass), email));

 //       ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Mail.xml");

//        MailUtils mu = (MailUtils) context.getBean("mailUtils");

//        mu.sendEmail("${emailUsername}", newTeacher.getEmail(), "Registration", mu.getActivationLink(newTeacher));

        return newTeacher;
    }

    @Override
    public Teacher activate(int userId) {
        Teacher teacher = teacherRepository.findOne(userId);

        teacher.activate();

        return teacher;
    }
}
