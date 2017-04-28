package ua.artcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import ua.artcode.model.User;

/**
 * Created by zhenia on 24.04.17.
 */
public class MailUtils {

    private MailSender mailSender;

    public void sendEmail(String myAdress, String email, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(myAdress);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(msg);

        mailSender.send(message);
    }

    public String getActivationLink(User user) {
        return user.getLogin() + ", to activate your account go to: ${serverLink} + /activate&id=" + user.getId();
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
