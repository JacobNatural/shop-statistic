package com.app.listener;

import com.app.persistence.entity.VerificationTokenEntity;
import com.app.persistence.repository.UserRepository;
import com.app.persistence.repository.VerificationTokenRepository;
import com.app.service.EmailService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.app.data.UserData.USER_ACTIVATION_DTO1;
import static com.app.data.UserData.USER_ENTITY1;

@ExtendWith(SpringExtension.class)
public class UserActivationListenerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserActivationListener userActivationListener;


    @Test
    @DisplayName("When a user is not found in the database, an exception is thrown.")
    public void test1() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                        () -> userActivationListener.sendActivationEmail(USER_ACTIVATION_DTO1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");

        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
    }


    @Test
    @DisplayName("When a user found in the database, token is save in data base and email is sent.")
    public void test2() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(USER_ENTITY1));

        ReflectionTestUtils.setField(userActivationListener, "activationMailExpireTime", 6000L);

        userActivationListener.sendActivationEmail(USER_ACTIVATION_DTO1);

        var inOder = Mockito.inOrder(userRepository, verificationTokenRepository, emailService);

        inOder.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        inOder.verify(verificationTokenRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(VerificationTokenEntity.class));
        inOder.verify(emailService, Mockito.times(1))
                .send(ArgumentMatchers.contains(USER_ENTITY1.getEmail()), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }
}
