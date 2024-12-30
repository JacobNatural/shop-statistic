package com.app.controller;
import com.app.converter.many.ClientsConverter;
import com.app.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
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
import java.util.List;

import static com.app.data.ClientData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientsConverter clientsConverter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("When creating a new client, then it should return the client ID")
    @SneakyThrows
    public void test1() {

        Mockito.when(clientService.addClient(CLIENT_DTO1))
                .thenReturn(1L);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(CLIENT_DTO1)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    @DisplayName("When creating a new clients, then it should return the clients ID")
    @SneakyThrows
    public void test2() {

        Mockito.when(clientService.addClients(ArgumentMatchers.anyList()))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(post("/clients/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(List.of(CLIENT_DTO1, CLIENT_DTO2))))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data", Matchers.contains(1, 2)));
    }

    @Test
    @DisplayName("When searching for a client by ID, then it should return the client details")
    @SneakyThrows
    public void test3() {

        Mockito.when(clientService.findById(1L))
                .thenReturn(CLIENT1);

        mockMvc.perform(get("/clients/1"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("When searching for a clients by ID, then it should return the clients details")
    @SneakyThrows
    public void test4() {

        Mockito.when(clientService.findAllByIds(List.of(1L, 2L)))
                .thenReturn(List.of(CLIENT1, CLIENT2));

        Mockito.when(clientsConverter.toDtoList(List.of(CLIENT1, CLIENT2)))
                .thenReturn(List.of(CLIENT_DTO1, CLIENT_DTO2));

        mockMvc.perform(get("/clients")
                        .queryParam("ids", "1")
                        .queryParam("ids", "2"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.data[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("When deleting a client by ID, then it should return the client ID")
    @SneakyThrows
    public void test5() {

        Mockito.when(clientService.removeElement(1L))
                .thenReturn(1L);

        mockMvc.perform(delete("/clients/1"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.is(1)));
    }

    @Test
    @DisplayName("When deleting a clients by ID, then it should return the clients ID")
    @SneakyThrows
    public void test6() {

        Mockito.when(clientService.removeAllByIds(List.of(1L, 2L)))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(delete("/clients")
                        .queryParam("ids", "1")
                        .queryParam("ids", "2"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", Matchers.is(1)))
                .andExpect(jsonPath("$.data[1]", Matchers.is(2)));
    }
}
