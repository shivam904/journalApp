package net.shivamcoder.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    private JavaMailSender javamailsender;

    @Autowired
    public EmailService(JavaMailSender javamailsender) {
        this.javamailsender = javamailsender;
    }

    public void sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            javamailsender.send(mailMessage);


        } catch (Exception e) {
            log.error("unable to send mail :", e);
        }


    }
}
