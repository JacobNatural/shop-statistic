package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.persistence.repository.UserRepository;
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

@ExtendWith(MockitoExtension.class)
public class UserServiceImplLostPasswordTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When resetting the password, if the email is not found, throw an EntityNotFoundException.")
    public void test1(){

        var emailDto = new EmailDto("aa@gmail.com");

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                userService.lostPassword(emailDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Email not found");

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When resetting the password, send an email with the token and return the user ID upon successful completion.")
    public void test2(){

        var emailDto = new EmailDto("aa@gmail.com");

        var inOrder = Mockito.inOrder(userRepository, eventPublisher);

        Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(USER_ENTITY1));

        Mockito.doNothing().when(eventPublisher).publishEvent(ArgumentMatchers.any(UserActivationDto.class));


        Assertions.assertThat(userService.lostPassword(emailDto))
                .isEqualTo(1L);

        inOrder.verify(userRepository, Mockito.times(1))
                .findByEmail(ArgumentMatchers.anyString());

        inOrder.verify(eventPublisher, Mockito.times(1))
                .publishEvent(ArgumentMatchers.any(UserActivationDto.class));

    }
}
