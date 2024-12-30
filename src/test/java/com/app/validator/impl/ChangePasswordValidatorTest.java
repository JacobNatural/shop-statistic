package com.app.validator.impl;

import com.app.controller.dto.user.ChangePasswordDto;
import com.app.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChangePasswordValidatorTest {

    @Mock
    private PasswordValidator passwordValidator;

    @InjectMocks
    private ChangePasswordValidator changePasswordValidator;

    @Test
    @DisplayName("When the current password is null, throw an IllegalArgumentException.")
    public void test1() {
        var changePassword = new ChangePasswordDto(null, "Abcde123!", "Abcde123!");
        Assertions.assertThatThrownBy(() ->
                        Validator.validate(changePassword, changePasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current password required");
    }

    @Test
    @DisplayName("When the new password confirmation is null, throw an IllegalArgumentException.")
    public void test2() {
        var changePassword = new ChangePasswordDto("Abbde123!", "Abcde123!", null);

        Assertions.assertThatThrownBy(() ->
                        Validator.validate(changePassword, changePasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("New password confirmation required");
    }

    @Test
    @DisplayName("When the new password is not valid, throw an IllegalArgumentException.")
    public void test3() {
        var changePassword = new ChangePasswordDto("Abbde123!", null, "Abcde123!");

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("Password cannot be null");

        Assertions.assertThatThrownBy(() ->
                        Validator.validate(changePassword, changePasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password cannot be null");
    }

    @Test
    @DisplayName("When the new password and password confirmation do not match, throw an IllegalArgumentException.")
    public void test4() {
        var changePassword = new ChangePasswordDto("Abbde123!", "Abcdf123!", "Abcde123!");

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("");

        Assertions.assertThatThrownBy(() ->
                        Validator.validate(changePassword, changePasswordValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Passwords confirmation do not match");
    }

    @Test
    @DisplayName("When all the passwords are valid, do not throw any exception.")
    public void test5() {
        var changePassword = new ChangePasswordDto("Abbde123!", "Abcde123!", "Abcde123!");

        Mockito.when(passwordValidator.validate(ArgumentMatchers.any()))
                .thenReturn("");

        Assertions.assertThatNoException().isThrownBy(() ->
                Validator.validate(changePassword, changePasswordValidator));
    }
}
