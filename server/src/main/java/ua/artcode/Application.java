package ua.artcode;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import ua.artcode.controller.CourseController;
import ua.artcode.controller.UserController;
import ua.artcode.enums.UserType;
import ua.artcode.exceptions.SuchCourseAlreadyExists;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;
import ua.artcode.model.dto.RegisterRequestDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {"ua.artcode"})
@EnableSwagger2 // http://localhost:8080/swagger-ui.html
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {

    @Value("${email.host}")
    private String mailHost;
    @Value("${email.port}")
    private int mailPort;
    @Value("${email.user}")
    private String mailUser;
    @Value("${email.pass}")
    private String mailPass;
    @Value("${email.properties.smtp.auth}")
    private String mailSmtpAuth;
    @Value("${email.properties.smtp.starttls.enable}")
    private String mailSmtpStartTLS;

    @Autowired
    private UserController userController;
    @Autowired
    private CourseController courseController;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.artcode.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUser);
        mailSender.setPassword(mailPass);

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", mailSmtpAuth);
        props.setProperty("mail.smtp.starttls.enable", mailSmtpStartTLS);

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    @Bean
    public InvocationRequest invocationRequest() {
        return new DefaultInvocationRequest();
    }

    @Bean
    public Invoker invoker() {
        return new DefaultInvoker();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return strings -> {

//            createCoursesAndLessons();

            try {
                userController.registerUser(
                        new RegisterRequestDTO("student",
                                "student@gmail.com",
                                "password",
                                UserType.STUDENT));
                userController.registerUser(
                        new RegisterRequestDTO("student2",
                                "student2@gmail.com",
                                "password",
                                UserType.STUDENT));
                userController.registerUser(
                        new RegisterRequestDTO("teacher",
                                "teacher@gmail.com",
                                "password",
                                UserType.TEACHER));

            } catch (Throwable ignored) {
            }
        };
    }

    // for test purposes - do not delete!
    private void createCoursesAndLessons() throws SuchCourseAlreadyExists {
        Map<Integer, String> courseName = new HashMap<>();
        Map<Integer, String> courseDescription = new HashMap<>();

        courseName.put(1, "Reflection API");
        courseName.put(2, "JDBC, Drivers, SQL");
        courseName.put(3, "Servlets, servlet containers, JSP");
        courseName.put(4, "REST/SOAP");
        courseName.put(5, "JPA, Hibernate");

        courseDescription.put(1, "Reflection API");
        courseDescription.put(2, "JDBC + SQL");
        courseDescription.put(3, "Java Servlet API");
        courseDescription.put(4, "Web Services");
        courseDescription.put(5, "Java JPA");


        for (int i = 1; i <= courseName.size(); i++) {


            courseController.addCourse(new Course(
                    courseName.get(i),
                    "Vlad Kornieiev",
                    "https://github.com/v21k/TestGitProject.git",
                    courseDescription.get(i),
                    "src/main/java",
                    "src/test/java"), null);

            for (int j = 0; j < 3; j++) {
                courseController.addLessonToCourse(i, new Lesson(
                                "Test lesson " + j,
                                "_02_lesson",
                                null,
                                null,
                                Collections.singletonList("src/test/java/_02_lesson/SolutionTests.java"),
                                "src/main/java/_02_lesson",
                                null,
                                "Test description"),
                        null
                );
            }
        }
    }
}
