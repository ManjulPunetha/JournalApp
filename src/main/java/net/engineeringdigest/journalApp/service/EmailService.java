package net.engineeringdigest.journalApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);           // Recipient email address
            mail.setSubject(subject); // Email subject line
            mail.setText(body);       // Email body/content

            javaMailSender.send(mail); // Send the email!
        } catch (MailException mailException) {
            log.error("Exception while sending email", mailException);
        }
    }
}
