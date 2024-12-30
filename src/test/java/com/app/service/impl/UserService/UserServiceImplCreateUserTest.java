package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.exception.ResourceAlreadyExistException;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.repository.UserRepository;
import com.app.service.impl.UserServiceImpl;
import com.app.validator.Validator;
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
public class UserServiceImplCreateUserTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When the username already exists, throw a ResourceAlreadyExistException.")
    public void test1(){

        try(@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var createUserDto = new CreateUserDto(
                    "az", "a", "aa", "aa1", "aa1", "aa@gmail.com");

            Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(USER_ENTITY1));

            Assertions.assertThatThrownBy(() ->
                            userService.createUser(createUserDto))
                    .isInstanceOf(ResourceAlreadyExistException.class)
                    .hasMessage("Username already exists");

            Mockito.verify(userRepository, Mockito.times(1))
                    .findByUsername(ArgumentMatchers.anyString());
        }
    }

    @Test
    @DisplayName("When the email already exists, throw a ResourceAlreadyExistException.")
    public void test2(){

        try(@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);


            var createUserDto = new CreateUserDto(
                    "az", "a", "aa", "aa1", "aa1", "aa@gmail.com");

            var inOrder = Mockito.inOrder(userRepository);

            Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.empty());

            Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(USER_ENTITY1));

            Assertions.assertThatThrownBy(() ->
                            userService.createUser(createUserDto))
                    .isInstanceOf(ResourceAlreadyExistException.class)
                    .hasMessage("Email already exists");

            inOrder.verify(userRepository, Mockito.times(1))
                    .findByUsername(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .findByEmail(ArgumentMatchers.anyString());
        }
    }

    @Test
    @DisplayName("When save the user and send email with token.")
    public void test3(){

        try(@SuppressWarnings("rawtypes") MockedStatic<Validator> mockedValidator = Mockito.mockStatic(Validator.class)) {
            mockedValidator.when(() -> Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);


            var createUserDto = new CreateUserDto(
                    "az", "a", "aa", "aa1", "aa1", "aa@gmail.com");

            var inOrder = Mockito.inOrder(userRepository,passwordEncoder,  eventPublisher);

            Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.empty());

            Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.empty());

            Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                    .thenReturn(USER_ENTITY1);

            Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                    .thenReturn("aa1");

            Assertions.assertThatNoException().isThrownBy(() ->
                            userService.createUser(createUserDto));


            inOrder.verify(userRepository, Mockito.times(1))
                    .findByUsername(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .findByEmail(ArgumentMatchers.anyString());

            inOrder.verify(passwordEncoder, Mockito.times(1))
                    .encode(ArgumentMatchers.anyString());

            inOrder.verify(userRepository, Mockito.times(1))
                    .save(createUserDto.toCreateUserEntity());

            inOrder.verify(eventPublisher, Mockito.times(1))
                    .publishEvent(ArgumentMatchers.any(UserActivationDto.class));
        }
    }
}
