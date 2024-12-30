package com.app.controller;

import com.app.controller.dto.user.CreateUserDto;
import com.app.exception.InvalidCredentialsException;
import com.app.exception.UserAlreadyActivatedException;
import com.app.security.service.TokenService;
import com.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLException;

import static com.app.data.UserData.CREATE_USER_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GlobalExceptionHandlerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("When an IllegalArgumentException is thrown, return the exception message.")
    @SneakyThrows
    public void test1(){

        Mockito.when(userService.createUser(CREATE_USER_DTO))
                .thenThrow(new IllegalArgumentException("Illegal argument exception"));

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(CREATE_USER_DTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Illegal argument exception"));
    }

    @Test
    @DisplayName("When an InvalidCredentialsException is thrown, return the exception message.")
    @SneakyThrows
    public void test2(){

        Mockito.when(userService.createUser(CREATE_USER_DTO))
                .thenThrow(new InvalidCredentialsException("Invalid credentials exception"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(CREATE_USER_DTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials exception"));
    }

    @Test
    @DisplayName("When an EntityNotFoundException is thrown, return the exception message.")
    @SneakyThrows
    public void test3(){

        Mockito.when(userService.createUser(CREATE_USER_DTO))
                .thenThrow(new EntityNotFoundException("Entity not found exception"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(CREATE_USER_DTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Entity not found exception"));
    }

    @Test
    @DisplayName("When an UserAlreadyActivatedException is thrown, return the exception message.")
    @SneakyThrows
    public void test4(){

        Mockito.when(userService.createUser(CREATE_USER_DTO))
                .thenThrow(new UserAlreadyActivatedException("User already activated exception"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(CREATE_USER_DTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User already activated exception"));
    }

    @Test
    @DisplayName("When an ResourceAlreadyExistException is thrown, return the exception message.")
    @SneakyThrows
    public void test5(){

        Mockito.when(userService.createUser(CREATE_USER_DTO))
                .thenThrow(new UserAlreadyActivatedException("Resource already exist exception"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(CREATE_USER_DTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Resource already exist exception"));
    }

    @Test
    @DisplayName("When an NullPointerException is thrown, return the exception message.")
    @SneakyThrows
    public void test6(){

        Mockito.when(userService.createUser(CREATE_USER_DTO))
                .thenThrow(new NullPointerException("Null pointer exception"));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(CREATE_USER_DTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Null pointer exception"));
    }
}
