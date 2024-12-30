package com.app.service.impl.UserService;

import com.app.controller.dto.user.*;
import com.app.model.Role;
import com.app.persistence.entity.UserEntity;
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

import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY1;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplChangeRoleTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("When changing a role and the user does not exist, throw an EntityNotFoundException.")
    public void test1(){
        var changeRoleDto = new ChangeRoleDto(1L, Role.ROLE_LEADER);

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.changeRole(changeRoleDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("When changing a role, return the user ID upon successful completion.")
    public void test2(){
        var changeRoleDto = new ChangeRoleDto(1L, Role.ROLE_LEADER);

        var inOrder = Mockito.inOrder(userRepository);

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(USER_ENTITY1));

        Mockito.when(userRepository.save(ArgumentMatchers.any(UserEntity.class)))
                .thenReturn(USER_ENTITY1);

        Assertions.assertThat(userService.changeRole(changeRoleDto))
                .isEqualTo(1L);

        inOrder.verify(userRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(userRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(UserEntity.class));
    }
}
