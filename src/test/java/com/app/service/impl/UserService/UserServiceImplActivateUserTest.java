package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.persistence.entity.UserEntity;
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

import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY1;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplActivateUserTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When trying to activate a user and the token does not exist, throw an EntityNotFoundException.")
    public void test1() {

        var userActivationTokenDto = new UserTokenActivationDto(1L, "token");

        Mockito.when(verificationTokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        userService.activateUser(userActivationTokenDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Token not found");

        Mockito.verify(verificationTokenRepository, Mockito.times(1))
                .findByToken(Mockito.anyString());
    }

    @Test
    @DisplayName("When trying to activate a user and the token is not  valid, return null.")
    public void test2() {

        var verificationTokenEntity = VerificationTokenEntity
                .builder()
                .id(1L)
                .token("token")
                .timeStamp(System.nanoTime() - 1)
                .build();

        var userActivationTokenDto = new UserTokenActivationDto(1L, "token");

        var inOrder = Mockito.inOrder(verificationTokenRepository);

        Mockito.when(verificationTokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(verificationTokenEntity));

        Assertions.assertThat(userService.activateUser(userActivationTokenDto))
                .isNull();

        inOrder.verify(verificationTokenRepository, Mockito.times(1))
                .findByToken(ArgumentMatchers.anyString());

        inOrder.verify(verificationTokenRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(VerificationTokenEntity.class));
    }

    @Test
    @DisplayName("When activating a user with a valid token, the user should be activated in the database..")
    public void test3() {

        var verificationTokenEntity = VerificationTokenEntity
                .builder()
                .id(1L)
                .token("token")
                .timeStamp(System.nanoTime() + 300000000000L)
                .user(USER_ENTITY1)
                .build();

        var userActivationTokenDto = new UserTokenActivationDto(1L, "token");

        var inOrder = Mockito.inOrder(verificationTokenRepository, userRepository);

        Mockito.when(verificationTokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(verificationTokenEntity));

        Assertions.assertThat(userService.activateUser(userActivationTokenDto))
                .isEqualTo(1L);

        inOrder.verify(verificationTokenRepository, Mockito.times(1))
                .findByToken(ArgumentMatchers.anyString());

        inOrder.verify(verificationTokenRepository, Mockito.times(1))
                .delete(ArgumentMatchers.any(VerificationTokenEntity.class));

        inOrder.verify(userRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(UserEntity.class));
    }
}
