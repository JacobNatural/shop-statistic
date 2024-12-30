package com.app.controller;

import com.app.controller.dto.order.OrdersAddDto;
import com.app.converter.many.ClientsConverter;
import com.app.converter.many.OrdersConverter;
import com.app.service.ClientService;
import com.app.service.OrderService;
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
import static com.app.data.OrderData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrdersConverter ordersConverter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("When creating a new order, then it should return the order ID")
    @SneakyThrows
    public void test1() {

        Mockito.when(orderService.addOrder(ORDER_ADD_DTO1))
                .thenReturn(1L);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ORDER_ADD_DTO1)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    @DisplayName("When creating a new orders, then it should return the orders ID")
    @SneakyThrows
    public void test2() {

        var ordersAddDto = new OrdersAddDto(1L, List.of(1L,2L));

        Mockito.when(orderService.addOrders(ordersAddDto))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(post("/orders/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ordersAddDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data", Matchers.contains(1, 2)));
    }

    @Test
    @DisplayName("When searching for a order by ID, then it should return the order details")
    @SneakyThrows
    public void test3() {

        Mockito.when(orderService.findById(1L))
                .thenReturn(ORDER1);

        mockMvc.perform(get("/orders/1"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("When searching for a orders by ID, then it should return the orders details")
    @SneakyThrows
    public void test4() {

        Mockito.when(orderService.findAllByIds(List.of(1L, 2L)))
                .thenReturn(List.of(ORDER1, ORDER2));

        Mockito.when(ordersConverter.toOrderFindDto(List.of(ORDER1, ORDER2)))
                .thenReturn(List.of(ORDER_FIND_DTO1, ORDER_FIND_DTO2));

        mockMvc.perform(get("/orders/all")
                        .queryParam("ids", "1")
                        .queryParam("ids", "2"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.data[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("When deleting a order by ID, then it should return the order ID")
    @SneakyThrows
    public void test5() {

        Mockito.when(orderService.removeElement(1L))
                .thenReturn(1L);

        mockMvc.perform(delete("/orders/1"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.is(1)));
    }

    @Test
    @DisplayName("When deleting a orders by ID, then it should return the orders ID")
    @SneakyThrows
    public void test6() {

        Mockito.when(orderService.removeAllByIds(List.of(1L, 2L)))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(delete("/orders/all")
                        .queryParam("ids", "1")
                        .queryParam("ids", "2"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", Matchers.is(1)))
                .andExpect(jsonPath("$.data[1]", Matchers.is(2)));
    }

}
