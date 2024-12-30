package com.app.controller;

import com.app.controller.dto.user.*;
import com.app.model.Role;
import com.app.security.dto.RefreshTokenDto;
import com.app.security.dto.TokensDto;
import com.app.security.service.TokenService;
import com.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("When creating a user, the system should return the user ID upon successful creation.")
    @SneakyThrows
    public void test1(){
        var createUser = new CreateUserDto("ab", "a", "b", "p", "p", "ab@gmail.com");

        Mockito.when(userService.createUser(createUser))
                .thenReturn(1L);

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(new ObjectMapper().writeValueAsString(createUser)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .createUser(createUser);
    }

    @Test
    @DisplayName("When activating a user, the system should return the user ID upon successful activation..")
    @SneakyThrows
    public void test2(){
        var userTokenActivationDto = new UserTokenActivationDto(1L, "token");

        Mockito.when(userService.activateUser(userTokenActivationDto))
                .thenReturn(1L);

        mockMvc.perform(post("/users/login/activate")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(userTokenActivationDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .activateUser(userTokenActivationDto);
    }

    @Test
    @DisplayName("When refreshing the activated token, the system should return the user ID upon successful token issuance.")
    @SneakyThrows
    public void test3(){
        var userEmailRefreshToken = new UserEmailRefreshToken("aa@gamil.com");

        Mockito.when(userService.refreshToken(userEmailRefreshToken))
                .thenReturn(1L);

        mockMvc.perform(post("/users/login/mail/refresh")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(userEmailRefreshToken)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .refreshToken(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("When refreshing the authorization token, the system should return the TokensDto.")
    @SneakyThrows
    public void test4(){
        var refreshTokenDto = new RefreshTokenDto("token");

        Mockito.when(tokenService.refreshToken(refreshTokenDto))
                .thenReturn(new TokensDto("accessToken", "refreshToken"));

        mockMvc.perform(post("/users/login/refresh")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(refreshTokenDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("refreshToken"));

        Mockito.verify(tokenService, Mockito.times(1))
                .refreshToken(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("When a password is lost, if the email with a token for a new password is invalid, throw an IllegalArgumentException.")
    @SneakyThrows
    public void test5(){
        var emailDto = new EmailDto("aa.gmail.com");

        Mockito.when(userService.lostPassword(emailDto))
                .thenReturn(1L);

        mockMvc.perform(post("/users/login/lost")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(emailDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email is invalid"));
    }

    @Test
    @DisplayName("When a password is lost, an email with a token for a new password should be sent, " +
            "and the system should return the user ID upon successful request.")
    @SneakyThrows
    public void test6(){
        var emailDto = new EmailDto("aa@gmail.com");

        Mockito.when(userService.lostPassword(emailDto))
                .thenReturn(1L);

        mockMvc.perform(post("/users/login/lost")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(emailDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .lostPassword(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("When saving a new password, the system should return the user ID upon a successful request.")
    @SneakyThrows
    public void test7(){
        var newPasswordDto = new NewPasswordDto("password","password", "token");

        Mockito.when(userService.newPassword(newPasswordDto))
                .thenReturn(1L);

        mockMvc.perform(patch("/users/login/password")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(newPasswordDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .newPassword(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("When changing an email, the system should return the user ID upon a successful request.")
    @SneakyThrows
    public void test8(){
        var emailDto = new EmailDto("aa@gmail.com");

        Mockito.when(userService.changeEmail(emailDto, "token"))
                .thenReturn(1L);

        mockMvc.perform(patch("/users/email")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(emailDto))
                        .header("Authorization","token"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .changeEmail(ArgumentMatchers.any(), ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When changing a password, the system should return the user ID upon a successful request.")
    @SneakyThrows
    public void test9(){
        var changePasswordDto = new ChangePasswordDto("current", "newPassword", "newPassword");

        Mockito.when(userService.changePassword(changePasswordDto, "token"))
                .thenReturn(1L);

        mockMvc.perform(patch("/users/password")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(changePasswordDto))
                        .header("Authorization","token"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .changePassword(ArgumentMatchers.any(), ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("When changing a role, the system should return the user ID upon a successful request.")
    @SneakyThrows
    public void test10(){
        var changeRoleDto = new ChangeRoleDto(1L, Role.ROLE_LEADER);

        Mockito.when(userService.changeRole(changeRoleDto))
                .thenReturn(1L);

        mockMvc.perform(patch("/users/role")
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .content(new ObjectMapper().writeValueAsString(changeRoleDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));

        Mockito.verify(userService, Mockito.times(1))
                .changeRole(ArgumentMatchers.any());
    }


}
