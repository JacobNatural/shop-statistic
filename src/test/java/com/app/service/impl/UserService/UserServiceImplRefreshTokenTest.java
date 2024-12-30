package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.exception.ResourceAlreadyExistException;
import com.app.exception.UserAlreadyActivatedException;
import com.app.persistence.entity.VerificationTokenEntity;
import com.app.persistence.repository.UserRepository;
import com.app.persistence.repository.VerificationTokenRepository;
import com.app.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY1;
import static com.app.data.UserData.USER_ENTITY_ACTIVATED_1;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplRefreshTokenTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When trying to send a refresh token for a non-existent user, throw an EntityNotFoundException.")
    public void test1() {

        var userEmailRefreshToken = new UserEmailRefreshToken("aa@gmail.com");

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        userService.refreshToken(userEmailRefreshToken))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When trying to send a refresh token for an already activated user, throw a UserAlreadyActivatedException.")
    public void test2() {

        var userEmailRefreshToken = new UserEmailRefreshToken("aa@gmail.com");

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(USER_ENTITY_ACTIVATED_1));

        Assertions.assertThatThrownBy(() ->
                        userService.refreshToken(userEmailRefreshToken))
                .isInstanceOf(UserAlreadyActivatedException.class)
                .hasMessage("User already activated");

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When trying to send a refresh token and a valid token already exists, throw a ResourceAlreadyExistException.")
    public void test3() {

        var verificationTokenEntity = VerificationTokenEntity
                .builder()
                .id(1L)
                .token("token")
                .user(USER_ENTITY1)
                .timeStamp(System.nanoTime() + 300000000000L)
                .build();

        var userEmailRefreshToken = new UserEmailRefreshToken("aa@gmail.com");

        var inOrder = Mockito.inOrder(userRepository, verificationTokenRepository);

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(USER_ENTITY1));

        Mockito.when(verificationTokenRepository.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(verificationTokenEntity));

        Assertions.assertThatThrownBy(() ->
                        userService.refreshToken(userEmailRefreshToken))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Token already exists");

        inOrder.verify(userRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        inOrder.verify(verificationTokenRepository, Mockito.times(1))
                .findByUserId(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("When sending a refresh token on email.")
    public void test4() {

        var verificationTokenEntity = VerificationTokenEntity
                .builder()
                .id(1L)
                .token("token")
                .user(USER_ENTITY1)
                .timeStamp(System.nanoTime())
                .build();

        var userEmailRefreshToken = new UserEmailRefreshToken("aa@gmail.com");

        var inOrder = Mockito.inOrder(userRepository, verificationTokenRepository, eventPublisher);

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(USER_ENTITY1));

        Mockito.when(verificationTokenRepository.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(verificationTokenEntity));

        Assertions.assertThat(userService.refreshToken(userEmailRefreshToken))
                .isEqualTo(1L);

        inOrder.verify(userRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        inOrder.verify(verificationTokenRepository, Mockito.times(1))
                .findByUserId(ArgumentMatchers.anyLong());

        inOrder.verify(verificationTokenRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(VerificationTokenEntity.class));

        inOrder.verify(eventPublisher, Mockito.times(1))
                .publishEvent(ArgumentMatchers.any(UserActivationDto.class));
    }
}
