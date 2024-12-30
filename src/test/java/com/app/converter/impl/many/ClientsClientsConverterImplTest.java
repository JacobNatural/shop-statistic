package com.app.converter.impl.many;

import com.app.converter.many.ClientsConverter;
import com.app.converter.many.impl.ClientsConverterImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.app.data.ClientData.*;

@SpringBootTest(classes = ClientsConverterImpl.class)
public class ClientsClientsConverterImplTest {

    @Autowired
    private ClientsConverter clientsConverter;

    @Test
    @DisplayName("When converting an empty list of clients to an empty list of client DTOs.")
    public void test1() {
        Assertions
                .assertThat(clientsConverter.toDtoList(List.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a list of clients to a list of client DTOs.")
    public void test2() {
        Assertions
                .assertThat(clientsConverter
                        .toDtoList(List.of(CLIENT1, CLIENT2, CLIENT3)))
                .contains(CLIENT_DTO1, CLIENT_DTO2, CLIENT_DTO3);
    }

    @Test
    @DisplayName("When converting an empty list of client DTOs to an empty list of client entities.")
    public void test3() {
        Assertions.assertThat(clientsConverter.toEntityList(List.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a list of client DTOs to a list of client entities.")
    public void test4() {
        Assertions.assertThat(clientsConverter.toEntityList(List.of(CLIENT_DTO1, CLIENT_DTO2, CLIENT_DTO3)))
                .contains(CLIENT_ENTITY1, CLIENT_ENTITY2, CLIENT_ENTITY3);
    }

    @Test
    @DisplayName("When converting an empty map of category and client to na empty list of GroupByDto.")
    public void test5() {
        Assertions.assertThat(clientsConverter.toCategoryAndMostClientDto(Map.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a map of category and client to a list of GroupByDto with String and ClientDto.")
    public void test6() {
        Assertions.assertThat(clientsConverter.toCategoryAndMostClientDto(Map.of(
                "groceries", List.of(CLIENT1, CLIENT2),
                "home", List.of(CLIENT3)
        ))).contains(GROUP_BY_CLIENT1, GROUP_BY_CLIENT2);
    }

    @Test
    @DisplayName("When converting an empty map of clint and debit to an empty list of ClientAndDebitDto.")
    public void test7() {
        Assertions.assertThat(clientsConverter.toCategoryAndMostClientDto(Map.of()))
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("When converting a map of clint and debit to a list of ClientAndDebitDto.")
    public void test8() {
        Assertions.assertThat(clientsConverter.toClientAndDebitDto(Map.of(
                        CLIENT1, BigDecimal.valueOf(-200),
                        CLIENT2, BigDecimal.valueOf(-520),
                        CLIENT3, BigDecimal.valueOf(-850)
                )))
                .contains(CLIENT_AND_DEBIT_DTO1, CLIENT_AND_DEBIT_DTO2, CLIENT_AND_DEBIT_DTO3);
    }

}
