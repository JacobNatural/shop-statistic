package com.app.validator.impl;

import com.app.controller.dto.user.CreateUserDto;
import com.app.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CreateUserDtoValidator.class})
public class CreateUserDtoValidatorTest {

    @Value("${validate.user.username.regex}")
    private String usernameRegex;
    @Value("${validate.user.username.min.length}")
    private int usernameMinLength;
    @Value("${validate.user.name.regex}")
    private String nameRegex;
    @Value("${validate.user.name.min.length}")
    private int nameMinLength;
    @Value("${validate.user.surname.regex}")
    private String surnameRegex;
    @Value("${validate.user.surname.min.length}")
    private int surnameMinLength;
    @Value("${validate.user.email.regex}")
    private String emailRegex;

    @MockBean
    private PasswordValidator passwordValidator;

    @Autowired
    private CreateUserDtoValidator createUserDtoValidator;

    @Test
    @DisplayName("When the user password is invalid, throw an IllegalArgumentException.")
    public void test1(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("Password cannot be null");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "Adam",
                "Nowak",null,
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password cannot be null");
    }

    @Test
    @DisplayName("When username is null, throw an IllegalArgumentException.")
    public void test2(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                null, "Adam",
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be null");
    }

    @Test
    @DisplayName("When username is too short, throw an IllegalArgumentException.")
    public void test3(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "ak", "Adam",
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username must be at least "  + usernameMinLength + " characters");
    }

    @Test
    @DisplayName("When username contains illegal characters, throw an IllegalArgumentException.")
    public void test4(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak!", "Adam",
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username contains illegal characters");
    }

    @Test
    @DisplayName("When name is null, throw an IllegalArgumentException.")
    public void test5(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", null,
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be null");
    }


    @Test
    @DisplayName("When name is too short, throw an IllegalArgumentException.")
    public void test6(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "a",
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name must be at least " + nameMinLength + " characters");
    }

    @Test
    @DisplayName("When name contains illegal characters, throw an IllegalArgumentException.")
    public void test7(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "adam!",
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name contains illegal characters");
    }

    @Test
    @DisplayName("When surname is null, throw an IllegalArgumentException.")
    public void test8(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "Adam",
                null,"AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Surname cannot be null");
    }

    @Test
    @DisplayName("When surname is too short, throw an IllegalArgumentException.")
    public void test9(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "Adam",
                "n","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Surname must be at least " + nameMinLength + " characters");
    }

    @Test
    @DisplayName("When surname contains illegal characters, throw an IllegalArgumentException.")
    public void test10(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "Adam",
                "nowak@","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Surname contains illegal characters");
    }

    @Test
    @DisplayName("When the password does not match the password confirmation, throw an IllegalArgumentException.")
    public void test11(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "Adam",
                "Nowak","AdamNowak12!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Passwords and password confirmation do not match");
    }

    @Test
    @DisplayName("When the email address is invalid, throw an IllegalArgumentException.")
    public void test12(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "Adam",
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@.com"
        );

        Assertions.assertThatThrownBy(() -> Validator.validate(crateUserDto, createUserDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email address is invalid");
    }

    @Test
    @DisplayName("When all the user's data is correct, do not throw an exception.")
    public void test13(){
        Mockito.when(passwordValidator.validate(ArgumentMatchers.anyString()))
                .thenReturn("");

        var crateUserDto = new CreateUserDto(
                "adamnowak", "Adam",
                "Nowak","AdamNowak123!",
                "AdamNowak123!","adamnowak@gmail.com"
        );

        Assertions.assertThatNoException().isThrownBy(()
                -> Validator.validate(crateUserDto, createUserDtoValidator));
    }



}

