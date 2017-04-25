package ua.artcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by zhenia on 24.04.17.
 */
public class MailUtils {

    @Autowired
    private MailSender mailSender;

    public void sendEmail(String myAdress, String email, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(myAdress);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(msg);

        mailSender.send(message);
    }
}
