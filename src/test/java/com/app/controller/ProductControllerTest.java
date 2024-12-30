package com.app.controller;

import com.app.controller.dto.product.ProductDto;
import com.app.controller.dto.product.ProductsFilterDto;
import com.app.converter.many.ProductsConverter;
import com.app.service.ProductService;
import com.app.validator.impl.ProductDtoValidator;
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
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.util.List;

import static com.app.data.ClientData.CLIENT_DTO1;
import static com.app.data.ClientData.CLIENT_DTO2;
import static com.app.data.ProductData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductsConverter productsConverter;

    @MockBean
    ProductDtoValidator productDtoValidator;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("When creating a new product, then it should return the product ID.")
    @SneakyThrows
    public void test1() {

        Mockito.when(productDtoValidator.supports(ArgumentMatchers.any()))
                        .thenReturn(true);

        Mockito.doNothing()
                .when(productDtoValidator)
                .validate(ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.when(productService.addProducts(PRODUCT_DTO1))
                .thenReturn(1L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(PRODUCT_DTO1)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    @DisplayName("When creating a new product, errors should be returned with the corresponding messages if the product is incorrect.")
    @SneakyThrows
    public void test2() {

        Mockito.when(productDtoValidator.supports(ArgumentMatchers.any()))
                .thenReturn(true);

        Mockito.doAnswer(invocationOnMock -> {
                    var args1 = (ProductDto) invocationOnMock.getArguments()[0];
                    var arg2 = (Errors) invocationOnMock.getArguments()[1];
                    arg2.rejectValue("name", "Name is invalid");
                    return null;
                })
                .when(productDtoValidator)
                .validate(ArgumentMatchers.any(Object.class), ArgumentMatchers.any(Errors.class));

        Mockito.when(productService.addProducts(PRODUCT_DTO1))
                .thenReturn(1L);

         mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(PRODUCT_DTO1)))
                 .andExpect(status().isBadRequest())
                 .andExpect(jsonPath("$.error").value("Name is invalid"));
    }

    @Test
    @DisplayName("When creating a new products, then it should return the products ID.")
    @SneakyThrows
    public void test3() {

        Mockito.doNothing()
                .when(productDtoValidator)
                .validate(ArgumentMatchers.any(), ArgumentMatchers.any());

        Mockito.when(productService.addProducts(ArgumentMatchers.anyList()))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(post("/products/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(List.of(CLIENT_DTO1, CLIENT_DTO2))))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data", Matchers.contains(1, 2)));
    }

    @Test
    @DisplayName("When creating a new products, errors should be returned with the corresponding messages if the any product is incorrect.")
    @SneakyThrows
    public void test4() {

        Mockito.when(productDtoValidator.supports(ArgumentMatchers.any()))
                        .thenReturn(true);

        Mockito.doAnswer(invocation ->{
                    var args1 = (ProductDto) invocation.getArguments()[0];
                    var arg2 = (Errors) invocation.getArguments()[1];
                    arg2.rejectValue("name", "Name is invalid");
                    return null;
                })
                .when(productDtoValidator)
                .validate(ArgumentMatchers.any(), ArgumentMatchers.any(Errors.class));


        Mockito.when(productService.addProducts(ArgumentMatchers.anyList()))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(post("/products/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(List.of(CLIENT_DTO1, CLIENT_DTO2))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Name is invalid"));

        Mockito.verify(productDtoValidator, Mockito.times(1))
                .validate(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("When searching for a product by ID, then it should return the product details")
    @SneakyThrows
    public void test5() {

        Mockito.when(productService.findById(1L))
                .thenReturn(PRODUCT1);

        mockMvc.perform(get("/products/1"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("When searching for a products by ID, then it should return the products details")
    @SneakyThrows
    public void test6() {

        Mockito.when(productService.findAllByIds(List.of(1L, 2L)))
                .thenReturn(List.of(PRODUCT1, PRODUCT2));

        Mockito.when(productsConverter.toDtoList(List.of(PRODUCT1, PRODUCT2)))
                .thenReturn(List.of(PRODUCT_DTO1, PRODUCT_DTO2));

        mockMvc.perform(get("/products")
                        .queryParam("ids", "1")
                        .queryParam("ids", "2"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.data[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("When deleting a product by ID, then it should return the product ID")
    @SneakyThrows
    public void test7() {

        Mockito.when(productService.removeElement(1L))
                .thenReturn(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.is(1)));
    }

    @Test
    @DisplayName("When deleting a clients by ID, then it should return the clients ID")
    @SneakyThrows
    public void test8() {

        Mockito.when(productService.removeAllByIds(List.of(1L, 2L)))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(delete("/products/all")
                        .queryParam("ids", "1")
                        .queryParam("ids", "2"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", Matchers.is(1)))
                .andExpect(jsonPath("$.data[1]", Matchers.is(2)));
    }

    @Test
    @DisplayName("When retrieving all available categories, it should return a list of categories.")
    @SneakyThrows
    public void test9() {

        Mockito.when(productService.getCategories())
                .thenReturn(List.of("home", "groceries"));

        mockMvc.perform(get("/products/categories"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.data[0]", Matchers.is("home")))
                .andExpect(jsonPath("$.data[1]", Matchers.is("groceries")));
    }

    @Test
    @DisplayName("When retrieving products with a filter, it should return a list of products.  ")
    @SneakyThrows
    public void test10() {
        var filterDto = new ProductsFilterDto(BigDecimal.ONE, BigDecimal.TEN, null);

        Mockito.when(productService.filterProducts(filterDto))
                .thenReturn(List.of(PRODUCT1, PRODUCT2));

        Mockito.when(productsConverter.toDtoList(List.of(PRODUCT1, PRODUCT2)))
                        .thenReturn(List.of(PRODUCT_DTO1, PRODUCT_DTO2));

        mockMvc.perform(post("/products/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(filterDto)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2 ));
    }
}
