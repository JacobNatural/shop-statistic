package com.app.service.impl.UserService;


import com.app.controller.dto.user.*;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.repository.UserRepository;
import com.app.persistence.repository.VerificationTokenRepository;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY1;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplChangeEmailTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private Validator<CreateUserDto> createUserValidator;

    @Mock
    private Validator<ChangePasswordDto> changePasswordDtoValidator;

    @Mock
    private Validator<NewPasswordDto> newPasswordDtoValidator;

    @Mock
    private Validator<EmailDto> emailDtoValidator;

    @Mock
    private CreateAdminUserDto createAdminUserDto;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When trying to change the email with a null token, throw an IllegalArgumentException.")
    public void test1() {

        var emailDto = new EmailDto("aa@gmail.com");

        Assertions.assertThatThrownBy(() ->
                        userService.changeEmail(emailDto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token cannot be null");
    }

    @Test
    @DisplayName("When trying to change email for invalid email, throw an IllegalArgumentException.")
    public void test2() {

        try (MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenThrow(new IllegalArgumentException("Email cannot be null"));

            var emailDto = new EmailDto(null);

            Assertions.assertThatThrownBy(() ->
                            userService.changeEmail(emailDto, "token"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Email cannot be null");
        }
    }

    @Test
    @DisplayName("When trying to change the email for a non-existing user, throw an EntityNotFoundException.")
    public void test3() {

        try (MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var inOrder = Mockito.inOrder(tokenService, userRepository);

            Mockito.when(tokenService.id(ArgumentMatchers.anyString()))
                    .thenReturn(1L);

            Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            var emailDto = new EmailDto(null);

            Assertions.assertThatThrownBy(() ->
                            userService.changeEmail(emailDto, "token"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("User not found");

            inOrder.verify(tokenService, Mockito.times(1))
                    .id(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .findById(ArgumentMatchers.anyLong());
        }
    }

    @Test
    @DisplayName("When channing the email, return the user ID upon successful completion.")
    public void test4() {

        try (MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var inOrder = Mockito.inOrder(tokenService, userRepository);

            var emailDto = new EmailDto("aa@gmail.com");

            Mockito.when(tokenService.id(ArgumentMatchers.anyString()))
                    .thenReturn(1L);

            Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(USER_ENTITY1));

            Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                            .thenReturn(USER_ENTITY1);

            Assertions.assertThat(userService.changeEmail(emailDto, "token"))
                    .isEqualTo(1L);

            inOrder.verify(tokenService, Mockito.times(1))
                    .id(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .findById(ArgumentMatchers.anyLong());

            inOrder.verify(userRepository, Mockito.times(1))
                    .save(ArgumentMatchers.any(UserEntity.class));
        }
    }
}
