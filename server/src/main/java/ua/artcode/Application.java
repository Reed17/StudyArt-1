package ua.artcode;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import ua.artcode.utils.AppPropertyHolder;
import ua.artcode.utils.CommandLineRunnerUtils;

import java.util.Properties;

@SpringBootApplication(scanBasePackages = {"ua.artcode"})
@EnableSwagger2 // http://localhost:8080/swagger-ui.html
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {

    @Autowired
    private AppPropertyHolder properties;
    @Autowired
    private CommandLineRunnerUtils commandLineRunnerUtils;

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
        mailSender.setHost(properties.getEmail().getHost());
        mailSender.setPort(properties.getEmail().getPort());
        mailSender.setUsername(properties.getEmail().getUser());
        mailSender.setPassword(properties.getEmail().getPassword());

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", properties.getEmail().getProperties().getSmtp().getAuth());
        props.setProperty("mail.smtp.starttls.enable", properties.getEmail().getProperties().getSmtp().getStarttls());

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
    public CommandLineRunner commandLineRunner() {
        return strings -> {
            // todo we do not need to comment each time, when out db was already inited
            commandLineRunnerUtils.createCoursesAndLessons();
            commandLineRunnerUtils.registerTestUsers();
        };
    }
}
