package com.app.validator.impl;

import com.app.controller.dto.user.NewPasswordDto;
import com.app.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewPasswordValidator.class)
public class NewPasswordValidatorTest {

    @MockBean
    private PasswordValidator passwordValidator;

    @Autowired
    private NewPasswordValidator newPasswordValidator;

    @Test
    @DisplayName("When the new password token is null, throw an IllegalArgumentException.")
    public void test1() {
        var newPassword = new NewPasswordDto("Adam1234!", "Adam1234!", null);

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("");

        Assertions.assertThatThrownBy(() ->
                        Validator.validate(newPassword, newPasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token cannot be null");
    }

    @Test
    @DisplayName("When the password confirmation is null, throw an IllegalArgumentException.")
    public void test2() {
        var newPassword = new NewPasswordDto("Adam1234!", null, "token");

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("");

        Assertions.assertThatThrownBy(() ->
                        Validator.validate(newPassword, newPasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("New password confirmation required");
    }

    @Test
    @DisplayName("When the password is invalid, throw an IllegalArgumentException.")
    public void test3() {
        var newPassword = new NewPasswordDto(null, "Adam1234!", "token");

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("Password cannot be null");

        Assertions.assertThatThrownBy(() ->
                        Validator.validate(newPassword, newPasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password cannot be null");
    }

    @Test
    @DisplayName("When the password do not match password confirmation, throw an IllegalArgumentException.")
    public void test4() {
        var newPassword = new NewPasswordDto("Adam12345!", "Adam1234!", "token");

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("");

        Assertions.assertThatThrownBy(() ->
                        Validator.validate(newPassword, newPasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Passwords confirmation do not match");
    }

    @Test
    @DisplayName("When the new password DTO data are correct, do not throw any exceptions..")
    public void test5() {
        var newPassword = new NewPasswordDto("Adam1234!", "Adam1234!", "token");

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("");

        Assertions.assertThatNoException().isThrownBy(() ->
                Validator.validate(newPassword, newPasswordValidator));
    }
}
