package com.app.controller;

import com.app.controller.dto.order.CategoryAndPriceStatisticDto;
import com.app.controller.dto.order.ClientAndDebitDto;
import com.app.controller.dto.order.GroupByDto;
import com.app.controller.dto.product.StatisticDto;
import com.app.converter.many.impl.ClientsConverterImpl;
import com.app.converter.many.impl.ProductsConverterImpl;
import com.app.converter.many.impl.ShopConverterImpl;
import com.app.service.ShopStatisticService;
import com.app.statistic.impl.StatisticImpl;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.app.data.ClientData.*;
import static com.app.data.ProductData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShopController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ShopControllerTest {

    @MockBean
    private ShopStatisticService shopStatisticService;

    @MockBean
    private ClientsConverterImpl clientsConverter;

    @MockBean
    private ShopConverterImpl shopConverterImpl;

    @MockBean
    private ProductsConverterImpl productsConverterImpl;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("When retrieving clients with larger payments, the system should return a list of such clients.")
    @SneakyThrows
    public void test1() {

        var inOrder = Mockito.inOrder(shopStatisticService, clientsConverter);

        Mockito.when(shopStatisticService.getClientWithBiggerPayment())
                .thenReturn(List.of(CLIENT1, CLIENT2));

        Mockito.when(clientsConverter.toDtoList(List.of(CLIENT1, CLIENT2)))
                .thenReturn(List.of(CLIENT_DTO1, CLIENT_DTO2));

        mockMvc.perform(get("/shop/clients/top"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2));

        inOrder.verify(shopStatisticService, Mockito.times(1))
                .getClientWithBiggerPayment();

        inOrder.verify(clientsConverter, Mockito.times(1))
                .toDtoList(ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("When retrieving clients with larger payments in a category, the system should return a list of those clients.")
    @SneakyThrows
    public void test2() {

        var inOrder = Mockito.inOrder(shopStatisticService, clientsConverter);

        Mockito.when(shopStatisticService.getClientWithBiggerPaymentInCategory("home"))
                .thenReturn(List.of(CLIENT1));

        Mockito.when(clientsConverter.toDtoList(List.of(CLIENT1)))
                .thenReturn(List.of(CLIENT_DTO1));

        mockMvc.perform(get("/shop/clients/top/home"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));

        inOrder.verify(shopStatisticService, Mockito.times(1))
                .getClientWithBiggerPaymentInCategory(ArgumentMatchers.anyString());

        inOrder.verify(clientsConverter, Mockito.times(1))
                .toDtoList(ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("When retrieving groups by age and the most frequent category, \t" +
            "the system should return a list of ages and their corresponding categories.")
    @SneakyThrows
    public void test3() {

        var inOrder = Mockito.inOrder(shopStatisticService, shopConverterImpl);

        Mockito.when(shopStatisticService.getAgeAndMostCategory())
                .thenReturn(Map.of(10, List.of("home")));

        Mockito.when(shopConverterImpl.toAgeMostCategoryDto(ArgumentMatchers.anyMap()))
                .thenReturn(List.of(new GroupByDto<>(10, List.of("home"))));

        mockMvc.perform(get("/shop/clients/age/category"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].key").value(10))
                .andExpect(jsonPath("$.data[0].elements", Matchers.containsInAnyOrder("home")));

        inOrder.verify(shopStatisticService, Mockito.times(1))
                .getAgeAndMostCategory();

        inOrder.verify(shopConverterImpl, Mockito.times(1))
                .toAgeMostCategoryDto(ArgumentMatchers.anyMap());
    }

    @Test
    @DisplayName("When retrieving category and price statistics, it should return a list of category and price statistics.")
    @SneakyThrows
    public void test4() {

        var inOrder = Mockito.inOrder(shopStatisticService, productsConverterImpl);

        Mockito.when(shopStatisticService.getCategoryAndPriceStatistic())
                .thenReturn(Map.of("home", new StatisticImpl(List.of(PRODUCT1), List.of(PRODUCT2), BigDecimal.TWO)));

        Mockito.when(productsConverterImpl.categoryAndPriceStatisticDto(ArgumentMatchers.anyMap()))
                .thenReturn(List.of(new CategoryAndPriceStatisticDto("home",
                        new StatisticDto(List.of(PRODUCT_DTO1), List.of(PRODUCT_DTO2), BigDecimal.TWO))));

        mockMvc.perform(get("/shop/products/category/price_statistic"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value("home"))
                .andExpect(jsonPath("$.data[0].statisticDto.min[0].id").value(1))
                .andExpect(jsonPath("$.data[0].statisticDto.max[0].id").value(2))
                .andExpect(jsonPath("$.data[0].statisticDto.avg").value(BigDecimal.TWO));

        inOrder.verify(shopStatisticService, Mockito.times(1))
                .getCategoryAndPriceStatistic();

        inOrder.verify(productsConverterImpl, Mockito.times(1))
                .categoryAndPriceStatisticDto(ArgumentMatchers.anyMap());
    }

    @Test
    @DisplayName("When retrieving grouping by age and most products, it should return a list of age groups and the most products.")
    @SneakyThrows
    public void test5() {

        var inOrder = Mockito.inOrder(shopStatisticService, productsConverterImpl);

        Mockito.when(shopStatisticService.getAgeAndMostProduct())
                .thenReturn(Map.of(20, List.of(PRODUCT1)));

        Mockito.when(productsConverterImpl.ageAndMostProductDto(ArgumentMatchers.anyMap()))
                .thenReturn(List.of(new GroupByDto<>(20, List.of(PRODUCT_DTO1))));

        mockMvc.perform(get("/shop/clients/age/product"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].key").value(20))
                .andExpect(jsonPath("$.data[0].elements[0].id").value(1));

        inOrder.verify(shopStatisticService, Mockito.times(1))
                .getAgeAndMostProduct();

        inOrder.verify(productsConverterImpl, Mockito.times(1))
                .ageAndMostProductDto(ArgumentMatchers.anyMap());
    }

    @Test
    @DisplayName("When grouping by category and the most clients, it should return a list of categories and their most clients.")
    @SneakyThrows
    public void test6() {

        var inOrder = Mockito.inOrder(shopStatisticService, clientsConverter);

        Mockito.when(shopStatisticService.getCategoryAndMostClient())
                .thenReturn(Map.of("home", List.of(CLIENT1)));

        Mockito.when(clientsConverter.toCategoryAndMostClientDto(ArgumentMatchers.anyMap()))
                .thenReturn(List.of(new GroupByDto<>("groceries", List.of(CLIENT_DTO1))));



        mockMvc.perform(get("/shop/clients/category"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].key").value("groceries"))
                .andExpect(jsonPath("$.data[0].elements[0].id").value(1));

        inOrder.verify(shopStatisticService, Mockito.times(1))
                .getCategoryAndMostClient();

        inOrder.verify(clientsConverter, Mockito.times(1))
                .toCategoryAndMostClientDto(ArgumentMatchers.anyMap());
    }

    @Test
    @DisplayName("When retrieving clients with debits, it should return a list of clients and their debits.")
    @SneakyThrows
    public void test7() {

        var inOrder = Mockito.inOrder(shopStatisticService, clientsConverter);

        Mockito.when(shopStatisticService.getClientsAndDebit())
                .thenReturn(Map.of(CLIENT1, BigDecimal.valueOf(-200)));

        Mockito.when(clientsConverter.toClientAndDebitDto(ArgumentMatchers.anyMap()))
                .thenReturn(List.of(new ClientAndDebitDto(CLIENT_DTO1, BigDecimal.valueOf(-200))));

        mockMvc.perform(get("/shop/clients/debits"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].clientDto.id").value(1))
                .andExpect(jsonPath("$.data[0].debitAmount").value(BigDecimal.valueOf(-200)));

        inOrder.verify(shopStatisticService, Mockito.times(1))
                .getClientsAndDebit();

        inOrder.verify(clientsConverter, Mockito.times(1))
                .toClientAndDebitDto(ArgumentMatchers.anyMap());
    }
}
