package com.app.validator.impl;

import com.app.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PasswordValidator.class)
public class PasswordValidatorTest {

    @Autowired
    private PasswordValidator validator;

    @Value("${validate.user.password.min.length}")
    private int passwordMinLength;

    @Test
    @DisplayName("When the password is null, throw an IllegalArgumentException.")
    public void test1() {

        Assertions.assertThatThrownBy(() -> Validator.validate(null, validator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password cannot be null");
    }

    @Test
    @DisplayName("When the password is too short, throw an IllegalArgumentException.")
    public void test2() {

        Assertions.assertThatThrownBy(() -> Validator.validate("adam", validator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password must be at least " + passwordMinLength + " characters");
    }

    @Test
    @DisplayName("When the password is too short, throw an IllegalArgumentException.")
    public void test3() {

        Assertions.assertThatThrownBy(() -> Validator.validate("adam", validator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password must be at least " + passwordMinLength + " characters");
    }

    static Stream<Arguments> passwordData() {
        return Stream.of(
                Arguments.of("password123!", "[A-Z]"),
                Arguments.of("PASSWORD123!", "[a-z]"),
                Arguments.of("Password!", "[0-9]"),
                Arguments.of("Password123", "[!@#$%^&*?]")
        );
    }

    @ParameterizedTest
    @MethodSource("passwordData")
    @DisplayName("When the password is too short, throw an IllegalArgumentException.")
    public void test4(String password, String regex) {

        Assertions.assertThatThrownBy(() -> Validator.validate(password, validator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password should have at least one: " + regex + " character");
    }

    @Test
    @DisplayName("When the password data are correct, do not throw any exception.")
    public void test5() {

        Assertions.assertThatNoException().isThrownBy(() ->
                Validator.validate("Password123!", validator));
    }
}
