package com.app.service.impl;

import com.app.service.EmailService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    EmailServiceImpl emailService;

    @Test
    @DisplayName("When sending email and invoke mail sender.")
    public void test1(){

        Mockito.doNothing().when(mailSender)
                .send(ArgumentMatchers.any(SimpleMailMessage.class));

        emailService.send("aa@gmail.com", "subject", "body");

        Mockito.verify(mailSender, Mockito.times(1))
                .send(ArgumentMatchers.any(SimpleMailMessage.class));
    }
}
