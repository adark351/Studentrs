package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendPaymentReminder(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("aminebaquouch32@gmail.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            System.out.println("Reminder sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email to: " + toEmail);
            e.printStackTrace();
        }
    }
}
