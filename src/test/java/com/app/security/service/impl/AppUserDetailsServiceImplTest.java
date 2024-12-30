package com.app.security.service.impl;

import com.app.persistence.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY_ACCESS_TOKEN_1;
import static com.app.model.Role.ROLE_ADMIN;

@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppUserDetailsServiceImpl appUserDetailsServiceImpl;

    @Test
    @DisplayName("When creating UserDetails and the user is not found, throw a EntityNotFoundException.")
    public void test1(){

        Mockito.when(userRepository.findByUsername("A"))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                appUserDetailsServiceImpl.loadUserByUsername("A"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("When creating UserDetails, return the correct UserDetails.")
    public void test2(){

        Mockito.when(userRepository.findByUsername("A"))
                .thenReturn(Optional.of(USER_ENTITY_ACCESS_TOKEN_1));

        var userDetails = new User("AB","aa",
                true, true,
                true, true,
                List.of(new SimpleGrantedAuthority(ROLE_ADMIN.toString())));

        Assertions.assertThat(appUserDetailsServiceImpl.loadUserByUsername("A"))
                .isEqualTo(userDetails);
    }

}
