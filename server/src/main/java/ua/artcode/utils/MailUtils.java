package ua.artcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import ua.artcode.model.User;

@Component
public class MailUtils {

    private final MailSender mailSender;
    private final AppPropertyHolder.Email email;

    @Autowired
    public MailUtils(MailSender mailSender, AppPropertyHolder appPropertyHolder) {
        this.mailSender = mailSender;
        this.email = appPropertyHolder.getEmail();
    }

    public void sendEmail(String myAdress, String email, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(myAdress);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(msg);

        // todo resolve conflict with emails limit
//        mailSender.send(message);
    }

    public String getActivationLink(User user) {
        return user.getUsername() +
                ", to activate your account go to: " +
                email.getServer() +
                "/activate&id=" +
                user.getId();
    }

    public MailSender getMailSender() {
        return mailSender;
    }
}
