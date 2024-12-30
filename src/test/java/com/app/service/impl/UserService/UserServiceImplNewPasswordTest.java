package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.entity.VerificationTokenEntity;
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
public class UserServiceImplNewPasswordTest {
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
    @DisplayName("When the NewPasswordDto is invalid, throw an IllegalArgumentException.")
    public void test1() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenThrow(new IllegalArgumentException("Token cannot be null"));

            var newPasswordDto = new NewPasswordDto("aa1", "aa1", null);

            Assertions.assertThatThrownBy(() ->
                            userService.newPassword(newPasswordDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Token cannot be null");
        }
    }

    @Test
    @DisplayName("When trying to a set new password and token is not found, throw an EntityNotFoundException.")
    public void test2() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            Mockito.when(verificationTokenRepository.findByToken(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.empty());

            var newPasswordDto = new NewPasswordDto("aa1", "aa1", "token");

            Assertions.assertThatThrownBy(() ->
                            userService.newPassword(newPasswordDto))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Token not found");

            Mockito.verify(verificationTokenRepository, Mockito.times(1))
                    .findByToken(ArgumentMatchers.anyString());
        }
    }

    @Test
    @DisplayName("When trying to a set new password and the token is expired, return null.")
    public void test3() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var verificationTokenEntity = VerificationTokenEntity
                    .builder()
                    .id(1L)
                    .token("token")
                    .timeStamp(System.nanoTime())
                    .user(USER_ENTITY1)
                    .build();

            var inOrder = Mockito.inOrder(verificationTokenRepository);

            Mockito.when(verificationTokenRepository.findByToken(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(verificationTokenEntity));

            var newPasswordDto = new NewPasswordDto("aa1", "aa1", "token");

            Assertions.assertThat(userService.newPassword(newPasswordDto))
                    .isNull();

            inOrder.verify(verificationTokenRepository, Mockito.times(1))
                    .findByToken(ArgumentMatchers.anyString());

            inOrder.verify(verificationTokenRepository, Mockito.times(1))
                    .delete(ArgumentMatchers.any(VerificationTokenEntity.class));
        }
    }

    @Test
    @DisplayName("When setting a new password, return the user ID upon successful completion.")
    public void test4() {
        try (@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var verificationTokenEntity = VerificationTokenEntity
                    .builder()
                    .id(1L)
                    .token("token")
                    .timeStamp(System.nanoTime() + 30000000000L)
                    .user(USER_ENTITY1)
                    .build();

            var inOrder = Mockito.inOrder(verificationTokenRepository, userRepository, passwordEncoder);

            Mockito.when(verificationTokenRepository.findByToken(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(verificationTokenEntity));

            Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                    .thenReturn("aa1");

            Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                    .thenReturn(USER_ENTITY1);

            var newPasswordDto = new NewPasswordDto("aa1", "aa1", "token");

            Assertions.assertThat(userService.newPassword(newPasswordDto))
                    .isEqualTo(1L);

            inOrder.verify(verificationTokenRepository, Mockito.times(1))
                    .findByToken(ArgumentMatchers.anyString());

            inOrder.verify(verificationTokenRepository, Mockito.times(1))
                    .delete(ArgumentMatchers.any(VerificationTokenEntity.class));

            inOrder.verify(passwordEncoder, Mockito.times(1))
                    .encode(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .save(ArgumentMatchers.any(UserEntity.class));
        }
    }
}
