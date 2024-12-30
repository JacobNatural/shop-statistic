package com.app.validator.impl;


import com.app.controller.dto.user.EmailDto;
import com.app.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmailDtoValidator.class)
public class EmailDtoValidatorTest{

    @Autowired
    private EmailDtoValidator emailDtoValidator;

    @Test
    @DisplayName("When the email address is null, throw an IllegalArgumentException.")
    public void test1(){

        var emailDto = new EmailDto(null);
        Assertions.assertThatThrownBy(() -> Validator.validate(emailDto, emailDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail cannot be null");
    }

    @Test
    @DisplayName("When the email address is invalid, throw an IllegalArgumentException.")
    public void test2(){

        var emailDto = new EmailDto("jakub.gmail.com");
        Assertions.assertThatThrownBy(() -> Validator.validate(emailDto, emailDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail does not match regex");
    }

    @Test
    @DisplayName("When the email address is correct, do not throw any exception.")
    public void test3(){

        var emailDto = new EmailDto("jakub@gmail.com");
        Assertions.assertThatNoException().isThrownBy(()
                -> Validator.validate(emailDto, emailDtoValidator));
    }
}
