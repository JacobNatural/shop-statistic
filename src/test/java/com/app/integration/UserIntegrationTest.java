package com.app.integration;

import com.app.ShopApplicationSpringBootApplication;
import com.app.controller.dto.order.OrderAddDto;
import com.app.controller.dto.user.CreateUserDto;
import com.app.controller.dto.user.UserTokenActivationDto;
import com.app.model.Role;
import com.app.persistence.entity.ClientEntity;
import com.app.persistence.entity.OrderEntity;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.repository.ClientRepository;
import com.app.persistence.repository.ProductRepository;
import com.app.persistence.repository.UserRepository;
import com.app.persistence.repository.VerificationTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.SneakyThrows;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.app.data.ClientData.CLIENT_ENTITY1;
import static com.app.data.ProductData.PRODUCT_ENTITY_1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShopApplicationSpringBootApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private GreenMail greenMail;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        greenMail = new GreenMail(new ServerSetup(2525, "localhost", "smtp"));
        greenMail.getUserManager().setAuthRequired(false);
        greenMail.start();
    }

    @Test
    @DisplayName("Integration test for user registration, activation, login, and order placement.")
    @SneakyThrows
    public void test1() {

        var userDto = new CreateUserDto("username", "Jakub", "Marjankowski", "Password12!",
                "Password12!", "foczka669@gmail.com");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.data").value(2));

        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .until(() -> greenMail.getReceivedMessages().length > 0);

        var messages = greenMail.getReceivedMessages();

        var userTokenActivationDto = new UserTokenActivationDto(
                2L, messages[0]
                .getContent()
                .toString()
                .replace("Use this code to activate your account: ", ""));

        mockMvc.perform(post("/users/login/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userTokenActivationDto)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.data").value(2));

        var jsonLogin = "{ \"username\": \"username\", \"password\": \"Password12!\" }";

        var loginResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn();

        clientRepository.save(CLIENT_ENTITY1);
        productRepository.save(PRODUCT_ENTITY_1);

        var orderAddDto = new OrderAddDto(1, 1);

        var accessToken = loginResult.getResponse().getCookie("AccessToken").getValue();

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(new ObjectMapper().writeValueAsString(orderAddDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(1));

        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("When trying to access a forbidden URI, the server responds with a '403 Forbidden' status.")
    @SneakyThrows
    public void test2() {

        var userEntity = UserEntity
                .builder()
                .name("Ala")
                .surname("Filak")
                .password(passwordEncoder.encode("Password12!"))
                .email("example@gmail.com")
                .enable(true)
                .username("username")
                .role(Role.ROLE_WORKER)
                .build();

        userRepository.save(userEntity);

        System.out.println(userRepository.findAll());

        var jsonLogin = "{ \"username\": \"username\", \"password\": \"Password12!\" }";

        var loginResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn();

        var accessToken = loginResult.getResponse().getCookie("AccessToken").getValue();

        mockMvc.perform(get("/clients/1")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error")
                        .value("Access Denied"));
    }


    @Test
    @DisplayName("When accessing /products without authentication, should return 403 Forbidden.")
    @SneakyThrows
    public void test3() {

        mockMvc.perform(get("/products"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error")
                        .value("Full authentication is required to access this resource"));
    }


    @AfterEach
    public void tearDown() {
        greenMail.stop();
    }


}
