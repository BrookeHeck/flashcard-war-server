package com.games.flashcard.service;

import com.games.flashcard.model.enums.EMAIL_PROPERTIES;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


@Service
public class EmailService {
    @Value("${mail.username}")
    String username;

    @Value("${mail.password}")
    String password;

    public void sendPasswordResetEmail(String firstName, String email) throws MessagingException {
        Message message = createEmail(email, createResetPasswordEmailBody(firstName));
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport("smtp");
        smtpTransport.connect(EMAIL_PROPERTIES.SMTP_SERVER.getProperty(), username, password);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(String email, String body) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress("support@mail.com"));
        message.setRecipients(RecipientType.TO, InternetAddress.parse(email, false));
        message.setSubject("Subject");
        message.setText(body);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(EMAIL_PROPERTIES.SMTP_HOST.getProperty(), EMAIL_PROPERTIES.SMTP_SERVER.getProperty());
        properties.put(EMAIL_PROPERTIES.SMTP_AUTH.getProperty(), true);
        properties.put(EMAIL_PROPERTIES.SMTP_PORT.getProperty(), 465);
        properties.put(EMAIL_PROPERTIES.SMTP_STARTTLS_ENABLE.getProperty(), true);
        properties.put(EMAIL_PROPERTIES.SMTP_STARTTLS_REQUIRED.getProperty(), true);
        return Session.getInstance(properties);
    }

    private String createResetPasswordEmailBody(String firstName) {
        return "Hello " + firstName + ", " +
                "\n" +
                "Your password has been reset. If this was not you please contact your organization." +
                "\n" +
                "V/r," +
                "\n" +
                "Flashcard Wars Support Team" +
                "\n\nPlease do not respond to this message";
    }
}
