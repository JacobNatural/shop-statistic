package com.app.data;

import com.app.controller.dto.user.CreateUserDto;
import com.app.controller.dto.user.UserActivationDto;
import com.app.model.Role;
import com.app.persistence.entity.UserEntity;

public interface UserData {

    CreateUserDto CREATE_USER_DTO =
            new CreateUserDto("ab", "a", "b", "p", "p", "ab@gmail.com");

    UserActivationDto USER_ACTIVATION_DTO1 =
            new UserActivationDto(1L);

    UserEntity USER_ENTITY1 = UserEntity
            .builder()
            .id(1L)
            .name("A")
            .surname("AA")
            .enable(false)
            .email("aa@gmail.com")
            .password("aa")
            .build();

    UserEntity USER_ENTITY_ACTIVATED_1 = UserEntity
            .builder()
            .id(1L)
            .name("A")
            .surname("AA")
            .username("AB")
            .enable(true)
            .email("aa@gmail.com")
            .password("aa")
            .build();

    UserEntity USER_ENTITY_ACCESS_TOKEN_1 = UserEntity
            .builder()
            .id(1L)
            .username("AB")
            .name("A")
            .surname("AA")
            .enable(true)
            .email("aa@gmail.com")
            .password("aa")
            .role(Role.ROLE_ADMIN)
            .build();
}
