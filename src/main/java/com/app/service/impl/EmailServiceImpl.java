package com.app.service.impl;

import com.app.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link EmailService} interface that provides functionality for sending emails.
 * <p>
 * This service uses {@link JavaMailSender} to send simple email messages with the provided recipient email,
 * subject, and body. It encapsulates the logic for constructing and sending email messages via SMTP.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    /**
     * The {@link JavaMailSender} instance used to send emails.
     */
    private final JavaMailSender mailSender;

    /**
     * Sends an email with the specified recipient, subject, and body.
     * <p>
     * This method creates a simple email message using {@link SimpleMailMessage}, sets the recipient's email address,
     * subject, and body, and then sends it using the {@link JavaMailSender}.
     * </p>
     *
     * @param email   the recipient's email address
     * @param subject the subject of the email
     * @param body    the body content of the email
     */
    @Override
    public void send(String email, String subject, String body) {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        mailSender.send(simpleMailMessage);
    }
}
