package com.app.service.impl.crud;

import com.app.controller.dto.ClientDto;
import com.app.converter.many.ClientsConverter;
import com.app.converter.single.Converter;
import com.app.exception.ResourceAlreadyExistException;
import com.app.model.Client;
import com.app.persistence.entity.ClientEntity;
import com.app.persistence.repository.ClientRepository;
import com.app.validator.Validator;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.app.data.ClientData.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private Converter<ClientEntity, Client> converter;

    @Mock
    private ClientsConverter clientsConverter;

    @InjectMocks
    private ClientServiceImpl service;

    @Test
    @DisplayName("When finding a client by ID, the element does not exist, so throw an EntityNotFoundException.")
    public void test1() {

        Mockito.when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        service.findById(ArgumentMatchers.anyLong()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Element not found");

        Mockito.verify(repository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("When finding a client by ID, and return client")
    public void test2() {

        var inOrder = Mockito.inOrder(repository, converter);

        Mockito.when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

        Mockito.when(converter.toModel(ArgumentMatchers.any(ClientEntity.class)))
                .thenReturn(CLIENT1);

        Assertions.assertThat(service.findById(ArgumentMatchers.anyLong()))
                .isEqualTo(CLIENT1);

        inOrder.verify(repository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(converter, Mockito.times(1))
                .toModel(ArgumentMatchers.any(ClientEntity.class));
    }

    @Test
    @DisplayName("When finding a clients by ID, the elements does not exist, so throw an EntityNotFoundException.")
    public void test3() {

        Mockito.when(repository.findAllById(ArgumentMatchers.anyList()))
                .thenReturn(List.of());

        Assertions.assertThatThrownBy(() ->
                        service.findAllByIds(List.of(1L, 2L)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not all elements were found.");

        Mockito.verify(repository, Mockito.times(1))
                .findAllById(ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("When finding clients by ID, not all elements were found, so an EntityNotFoundException is thrown.")
    public void test4() {

        var inOrder = Mockito.inOrder(repository, converter);

        Mockito.when(repository.findAllById(ArgumentMatchers.anyList()))
                .thenReturn(List.of(CLIENT_ENTITY_READ_1));

        Mockito.when(converter.toModel(CLIENT_ENTITY_READ_1))
                .thenReturn(CLIENT1);

        Assertions.assertThatThrownBy(() ->
                        service.findAllByIds(List.of(1L, 2L)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not all elements were found.");

        inOrder.verify(repository, Mockito.times(1))
                .findAllById(ArgumentMatchers.anyList());

        inOrder.verify(converter, Mockito.times(1))
                .toModel(ArgumentMatchers.any(ClientEntity.class));
    }

    @Test
    @DisplayName("When finding clients by ID, return the corresponding clients.")
    public void test5() {

        var inOrder = Mockito.inOrder(repository, converter);

        Mockito.when(repository.findAllById(ArgumentMatchers.anyList()))
                .thenReturn(List.of(CLIENT_ENTITY_READ_1, CLIENT_ENTITY_READ_2));

        Mockito.when(converter.toModel(CLIENT_ENTITY_READ_1))
                .thenReturn(CLIENT1);

        Mockito.when(converter.toModel(CLIENT_ENTITY_READ_2))
                .thenReturn(CLIENT2);

        Assertions.assertThat(service.findAllByIds(List.of(1L, 2L)))
                .isEqualTo(List.of(CLIENT1, CLIENT2));

        inOrder.verify(repository, Mockito.times(1))
                .findAllById(ArgumentMatchers.anyList());

        inOrder.verify(converter, Mockito.times(2))
                .toModel(ArgumentMatchers.any(ClientEntity.class));
    }


    @Test
    @DisplayName("When removing a client by ID, return the ID of the removed client.")
    public void test6() {

        var inOrder = Mockito.inOrder(repository, converter);

        Mockito.when(repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

        Mockito.when(converter.toModel(CLIENT_ENTITY_READ_1))
                .thenReturn(CLIENT1);

        Mockito.when(converter.toEntity(CLIENT1))
                .thenReturn(CLIENT_ENTITY_READ_1);

        Assertions.assertThat(service.removeElement(1L))
                .isEqualTo(1L);

        inOrder.verify(repository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(converter, Mockito.times(1))
                .toModel(ArgumentMatchers.any(ClientEntity.class));

        inOrder.verify(converter, Mockito.times(1))
                .toEntity(ArgumentMatchers.any(Client.class));

        inOrder.verify(repository, Mockito.times(1))
                .delete(ArgumentMatchers.any(ClientEntity.class));
    }

    @Test
    @DisplayName("When removing a client by ID, return the ID of the removed client.")
    public void test7() {

        var inOrder = Mockito.inOrder(repository, converter);

        Mockito.when(repository.findAllById(ArgumentMatchers.anyList()))
                .thenReturn(List.of(CLIENT_ENTITY_READ_1, CLIENT_ENTITY_READ_2));

        Mockito.when(converter.toModel(CLIENT_ENTITY_READ_1))
                .thenReturn(CLIENT1);

        Mockito.when(converter.toModel(CLIENT_ENTITY_READ_2))
                .thenReturn(CLIENT2);

        Mockito.when(converter.toEntity(CLIENT1))
                .thenReturn(CLIENT_ENTITY_READ_1);

        Mockito.when(converter.toEntity(CLIENT2))
                .thenReturn(CLIENT_ENTITY_READ_2);


        Assertions.assertThat(service.removeAllByIds(List.of(1L, 2L)))
                .isEqualTo(List.of(1L, 2L));

        inOrder.verify(repository, Mockito.times(1))
                .findAllById(ArgumentMatchers.anyList());

        inOrder.verify(converter, Mockito.times(2))
                .toModel(ArgumentMatchers.any(ClientEntity.class));

        inOrder.verify(converter, Mockito.times(2))
                .toEntity(ArgumentMatchers.any(Client.class));

        inOrder.verify(repository, Mockito.times(1))
                .deleteAll(ArgumentMatchers.anyList());
    }


    @Test
    @DisplayName("When adding a client, for invalid client data, throw an IllegalArgumentException.")
    public void test8() {
        try (MockedStatic<Validator> mockedStatic = Mockito.mockStatic(Validator.class)) {
            mockedStatic.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenThrow(new IllegalArgumentException("Client name cannot be null"));

            var clientDto = new ClientDto(1L, null, "A", 10, BigDecimal.ONE);

            Assertions.assertThatThrownBy(() -> service.addClient(clientDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Client name cannot be null");
        }
    }

    @Test
    @DisplayName("When adding a client and client already exist, throw an ResourceAlreadyExistException.")
    public void test9() {
        try (MockedStatic<Validator> mockedStatic = Mockito.mockStatic(Validator.class)) {
            mockedStatic.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            Mockito.when(repository.findByNameAndSurname(
                            ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

            var clientDto = new ClientDto(1L, "A", "B", 10, BigDecimal.ONE);

            Assertions.assertThatThrownBy(() -> service.addClient(clientDto))
                    .isInstanceOf(ResourceAlreadyExistException.class)
                    .hasMessage("Client already exists");

            Mockito.verify(repository, Mockito.times(1))
                    .findByNameAndSurname(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        }
    }

    @Test
    @DisplayName("When adding a client, return the client ID upon successful completion.")
    public void test10() {
        try (MockedStatic<Validator> mockedStatic = Mockito.mockStatic(Validator.class)) {
            mockedStatic.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var inOrder = Mockito.inOrder(repository);

            Mockito.when(repository.findByNameAndSurname(
                            ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(Optional.empty());

            Mockito.when(repository.save(ArgumentMatchers.any(ClientEntity.class)))
                    .thenReturn(CLIENT_ENTITY_READ_1);

            var clientDto = new ClientDto(1L, "A", "B", 10, BigDecimal.ONE);

            Assertions.assertThat(service.addClient(clientDto))
                    .isEqualTo(1L);

            inOrder.verify(repository, Mockito.times(1))
                    .findByNameAndSurname(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

            inOrder.verify(repository, Mockito.times(1))
                    .save(ArgumentMatchers.any(ClientEntity.class));
        }
    }

    @Test
    @DisplayName("When adding clients, for invalid client data, throw an IllegalArgumentException.")
    public void test11() {
        try (MockedStatic<Validator> mockedStatic = Mockito.mockStatic(Validator.class)) {
            mockedStatic.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenThrow(new IllegalArgumentException("One client name cannot be null"));

            var clientDto1 = new ClientDto(1L, null, "B", 10, BigDecimal.ONE);
            var clientDto2 = new ClientDto(1L, "A", "BB", 20, BigDecimal.TWO);

            Assertions.assertThatThrownBy(() ->
                            service.addClients(List.of(clientDto1, clientDto2)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("One client name cannot be null");
        }
    }

    @Test
    @DisplayName("When adding clients and one of the clients already exists, throw an ResourceAlreadyExistException.")
    public void test12() {
        try (MockedStatic<Validator> mockedStatic = Mockito.mockStatic(Validator.class)) {
            mockedStatic.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            Mockito.when(repository.findByNameAndSurname("A", "B"))
                    .thenReturn(Optional.empty());

            Mockito.when(repository.findByNameAndSurname("AA", "BB"))
                    .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

            var clientDto1 = new ClientDto(1L, "A", "B", 10, BigDecimal.ONE);
            var clientDto2 = new ClientDto(1L, "AA", "BB", 20, BigDecimal.TWO);

            Assertions.assertThatThrownBy(() ->
                            service.addClients(List.of(clientDto1, clientDto2)))
                    .isInstanceOf(ResourceAlreadyExistException.class)
                    .hasMessage("Client: %s already exists".formatted(clientDto2));

            Mockito.verify(repository, Mockito.times(2))
                    .findByNameAndSurname(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        }
    }

    @Test
    @DisplayName("When adding clients, return the clients ID upon successful completion.")
    public void test13() {
        try (MockedStatic<Validator> mockedStatic = Mockito.mockStatic(Validator.class)) {
            mockedStatic.when(() ->
                            Validator.validate(ArgumentMatchers.any(), ArgumentMatchers.any()))
                    .thenAnswer(invocation -> null);

            var inOrder = Mockito.inOrder(repository, clientsConverter);

            Mockito.when(repository.findByNameAndSurname("A", "B"))
                    .thenReturn(Optional.empty());

            Mockito.when(repository.findByNameAndSurname("AA", "BB"))
                    .thenReturn(Optional.empty());

            Mockito.when(repository.saveAll(ArgumentMatchers.anyList()))
                    .thenReturn(List.of(CLIENT_ENTITY_READ_1, CLIENT_ENTITY_READ_2));

            Mockito.when(clientsConverter.toEntityList(ArgumentMatchers.anyList()))
                    .thenReturn(List.of(CLIENT_ENTITY_READ_1, CLIENT_ENTITY_READ_2));

            var clientDto1 = new ClientDto(1L, "A", "B", 10, BigDecimal.ONE);
            var clientDto2 = new ClientDto(1L, "AA", "BB", 20, BigDecimal.TWO);

            Assertions.assertThat(service.addClients(List.of(clientDto1, clientDto2)))
                    .isEqualTo(List.of(1L, 2L));

            inOrder.verify(repository, Mockito.times(2))
                    .findByNameAndSurname(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

            inOrder.verify(clientsConverter, Mockito.times(1))
                    .toEntityList(ArgumentMatchers.anyList());

            inOrder.verify(repository, Mockito.times(1))
                    .saveAll(ArgumentMatchers.anyList());
        }
    }
}
