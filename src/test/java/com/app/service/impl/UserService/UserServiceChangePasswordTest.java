package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.exception.InvalidCredentialsException;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.repository.UserRepository;
import com.app.security.service.TokenService;
import com.app.service.impl.UserServiceImpl;
import com.app.validator.Validator;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY1;

@ExtendWith(MockitoExtension.class)
public class UserServiceChangePasswordTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When changing a password and the token is null, throw an IllegalArgumentException.")
    public void test1() {
        var changePasswordDto = new ChangePasswordDto("current", "newPassword", "newPassword");

        Assertions.assertThatThrownBy(() ->
                        userService.changePassword(changePasswordDto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token cannot be null");
    }

    @Test
    @DisplayName("When changing a password and the ChangePasswordDto.class is incorrect, throw an IllegalArgumentException.")
    public void test2() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenThrow(new IllegalArgumentException("Current password required"));

            var changePasswordDto = new ChangePasswordDto(null, "newPassword", "newPassword");

            Assertions.assertThatThrownBy(() ->
                            userService.changePassword(changePasswordDto, "token"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Current password required");
        }
    }

    @Test
    @DisplayName("When changing a password and user do not exist, throw an EntityNotFoundException.")
    public void test3() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var inOrder = Mockito.inOrder(tokenService, userRepository);

            Mockito.when(tokenService.id(ArgumentMatchers.anyString()))
                    .thenReturn(1L);

            Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            var changePasswordDto = new ChangePasswordDto("current", "newPassword", "newPassword");

            Assertions.assertThatThrownBy(() ->
                            userService.changePassword(changePasswordDto, "token"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("User not found");

            inOrder.verify(tokenService, Mockito.times(1))
                    .id(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .findById(ArgumentMatchers.anyLong());
        }
    }

    @Test
    @DisplayName("When changing a password and the current password is incorrect, throw an EntityNotFoundException.")
    public void test4() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var inOrder = Mockito.inOrder(tokenService, userRepository, passwordEncoder);

            Mockito.when(tokenService.id(ArgumentMatchers.anyString()))
                    .thenReturn(1L);

            Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(USER_ENTITY1));

            Mockito.when(passwordEncoder.matches(
                    ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(false);


            var changePasswordDto = new ChangePasswordDto("current", "newPassword", "newPassword");

            Assertions.assertThatThrownBy(() ->
                            userService.changePassword(changePasswordDto, "token"))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Passwords do not match");

            inOrder.verify(tokenService, Mockito.times(1))
                    .id(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .findById(ArgumentMatchers.anyLong());

            inOrder.verify(passwordEncoder, Mockito.times(1))
                    .matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        }
    }

    @Test
    @DisplayName("When a user's password is changed, return the user ID upon successful completion.")
    public void test5() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var inOrder = Mockito.inOrder(tokenService, userRepository, passwordEncoder);

            Mockito.when(tokenService.id(ArgumentMatchers.anyString()))
                    .thenReturn(1L);

            Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(USER_ENTITY1));

            Mockito.when(passwordEncoder.matches(
                            ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(true);

            Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                    .thenReturn("newPassword");

            Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                    .thenReturn(USER_ENTITY1);


            var changePasswordDto = new ChangePasswordDto("current", "newPassword", "newPassword");

            Assertions.assertThat(userService.changePassword(changePasswordDto, "token"))
                    .isEqualTo(1L);

            inOrder.verify(tokenService, Mockito.times(1))
                    .id(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .findById(ArgumentMatchers.anyLong());

            inOrder.verify(passwordEncoder, Mockito.times(1))
                    .matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

            inOrder.verify(passwordEncoder, Mockito.times(1))
                    .encode(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .save(ArgumentMatchers.any(UserEntity.class));
        }
    }
}
