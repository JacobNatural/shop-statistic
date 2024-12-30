package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.exception.ResourceAlreadyExistException;
import com.app.persistence.repository.UserRepository;
import com.app.security.service.TokenService;
import com.app.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY1;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplInitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CreateAdminUserDto createAdminUserDto;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When the user already exist in data base, throw a ResourceAlreadyExistException")
    public void test1() {

        var inOrder = Mockito.inOrder(userRepository, createAdminUserDto);

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(USER_ENTITY1));

        Mockito.when(createAdminUserDto.username())
                .thenReturn("AA");

        Assertions.assertThatThrownBy(() -> userService.init())
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Username already exists");

        inOrder.verify(createAdminUserDto, times(1))
                .username();

        inOrder.verify(userRepository, times(1))
                .findByUsername(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When create admin user, and add to data base")
    public void test2() {

        var inOrder = Mockito.inOrder(userRepository, createAdminUserDto, passwordEncoder);
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(createAdminUserDto.username())
                .thenReturn("AA");

        Mockito.when(createAdminUserDto.toUserEntity())
                .thenReturn(USER_ENTITY1);

        Mockito.when(createAdminUserDto.password())
                .thenReturn("aa");

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                .thenReturn("aa");

        Assertions.assertThatNoException().isThrownBy(() -> userService.init());

        inOrder.verify(createAdminUserDto, times(1))
                .username();

        inOrder.verify(userRepository, times(1))
                .findByUsername(ArgumentMatchers.anyString());

        inOrder.verify(createAdminUserDto, times(1))
                .toUserEntity();

        inOrder.verify(passwordEncoder, times(1))
                .encode(ArgumentMatchers.anyString());

        inOrder.verify(userRepository, times(1))
                .save(USER_ENTITY1);

    }


}
