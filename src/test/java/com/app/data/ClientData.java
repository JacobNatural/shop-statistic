package com.app.data;

import com.app.controller.dto.ClientDto;
import com.app.controller.dto.order.ClientAndDebitDto;
import com.app.controller.dto.order.GroupByDto;
import com.app.model.Client;
import com.app.persistence.entity.ClientEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ClientData {

    Client CLIENT1 = new Client(
            1L, "A", "AA", 30, BigDecimal.valueOf(2000));

    Client CLIENT2 = new Client(
            2L, "B", "BB", 30, BigDecimal.valueOf(1500));

    Client CLIENT3 = new Client(
            3L, "C", "CC", 11, BigDecimal.valueOf(360));

    ClientDto CLIENT_DTO1 = new ClientDto(
            1L, "A", "AA", 30, BigDecimal.valueOf(2000));

    ClientDto CLIENT_DTO2 = new ClientDto(
            2L, "B", "BB", 30, BigDecimal.valueOf(1500));

    ClientDto CLIENT_DTO3 = new ClientDto(
            3L, "C", "CC", 11, BigDecimal.valueOf(360));

    ClientEntity CLIENT_ENTITY1 = ClientEntity
            .builder()
            .name("A")
            .surname("AA")
            .age(30)
            .cash(BigDecimal.valueOf(2000))
            .build();

    ClientEntity CLIENT_ENTITY2 = ClientEntity
            .builder()
            .name("B")
            .surname("BB")
            .age(30)
            .cash(BigDecimal.valueOf(1500))
            .build();

    ClientEntity CLIENT_ENTITY3 = ClientEntity
            .builder()
            .name("C")
            .surname("CC")
            .age(11)
            .cash(BigDecimal.valueOf(360))
            .build();

    ClientEntity CLIENT_ENTITY_READ_1 = ClientEntity
            .builder()
            .id(1L)
            .name("A")
            .surname("AA")
            .age(30)
            .cash(BigDecimal.valueOf(2000))
            .build();

    ClientEntity CLIENT_ENTITY_READ_2 = ClientEntity
            .builder()
            .id(2L)
            .name("B")
            .surname("BB")
            .age(30)
            .cash(BigDecimal.valueOf(1500))
            .build();

    ClientEntity CLIENT_ENTITY_READ_3 = ClientEntity
            .builder()
            .id(3L)
            .name("C")
            .surname("CC")
            .age(11)
            .cash(BigDecimal.valueOf(360))
            .build();

    ClientEntity CLIENT_ENTITY_READ_4 = ClientEntity
            .builder()
            .id(4L)
            .name("D")
            .surname("DD")
            .age(14)
            .cash(BigDecimal.valueOf(460))
            .build();

    ClientEntity CLIENT_ENTITY_READ_5 = ClientEntity
            .builder()
            .id(5L)
            .name("E")
            .surname("EE")
            .age(16)
            .cash(BigDecimal.valueOf(20))
            .build();
    ClientEntity CLIENT_ENTITY_READ_6 = ClientEntity
            .builder()
            .id(6L)
            .name("F")
            .surname("FF")
            .age(24)
            .cash(BigDecimal.valueOf(40))
            .build();


    GroupByDto<String, ClientDto> GROUP_BY_CLIENT1 = new GroupByDto<>(
            "groceries", List.of(CLIENT_DTO1, CLIENT_DTO2));

    GroupByDto<String, ClientDto> GROUP_BY_CLIENT2 = new GroupByDto<>(
            "home", List.of(CLIENT_DTO3));

    ClientAndDebitDto CLIENT_AND_DEBIT_DTO1 = new ClientAndDebitDto(
            CLIENT_DTO1, BigDecimal.valueOf(-200));

    ClientAndDebitDto CLIENT_AND_DEBIT_DTO2 = new ClientAndDebitDto(
            CLIENT_DTO2, BigDecimal.valueOf(-520));

    ClientAndDebitDto CLIENT_AND_DEBIT_DTO3 = new ClientAndDebitDto(
            CLIENT_DTO3, BigDecimal.valueOf(-850));
}

