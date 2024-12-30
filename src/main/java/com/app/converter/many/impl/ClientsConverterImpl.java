package com.app.converter.many.impl;

import com.app.controller.dto.order.GroupByDto;
import com.app.converter.many.ClientsConverter;
import com.app.model.Client;
import com.app.controller.dto.order.ClientAndDebitDto;
import com.app.controller.dto.ClientDto;
import com.app.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link ClientsConverter} interface for converting between
 * different representations of clients.
 *
 * <p>This component handles the conversion of client data from entities to DTOs
 * and vice versa. It also provides helper methods for grouping clients by category
 * and associating clients with their respective debit amounts.</p>
 */
@Component
public class ClientsConverterImpl implements ClientsConverter {

    /**
     * Converts a list of {@link Client} entities to a list of {@link ClientDto}.
     *
     * @param clients the list of {@link Client} entities to convert
     * @return a list of {@link ClientDto} representing the clients
     */
    @Override
    public List<ClientDto> toDtoList(List<Client> clients) {
        return clients
                .stream()
                .map(Client::toClientDto)
                .toList();
    }

    /**
     * Converts a list of {@link ClientDto} to a list of {@link ClientEntity}.
     *
     * @param dtos the list of {@link ClientDto} to convert
     * @return a list of {@link ClientEntity} representing the clients
     */
    @Override
    public List<ClientEntity> toEntityList(List<ClientDto> dtos) {
        return dtos
                .stream()
                .map(ClientDto::toClientEntity)
                .toList();
    }

    /**
     * Converts a map of category names to a list of clients and groups them into
     * {@link GroupByDto} objects.
     *
     * @param map a map where the key is a category name and the value is a list of {@link Client} objects
     * @return a list of {@link GroupByDto} objects, each containing a category and a list of clients in that category
     */
    @Override
    public List<GroupByDto<String, ClientDto>> toCategoryAndMostClientDto(Map<String, List<Client>> map) {
        return map
                .entrySet()
                .stream()
                .map(m -> new GroupByDto<>(
                        m.getKey(), toDtoList(m.getValue())))
                .toList();
    }

    /**
     * Converts a map of {@link Client} to their respective debit amounts into a list
     * of {@link ClientAndDebitDto}.
     *
     * @param map a map where the key is a {@link Client} and the value is the corresponding debit amount
     * @return a list of {@link ClientAndDebitDto} objects, each containing a client and their debit amount
     */
    @Override
    public List<ClientAndDebitDto> toClientAndDebitDto(Map<Client, BigDecimal> map) {
        return map
                .entrySet()
                .stream()
                .map(m -> new ClientAndDebitDto(m.getKey().toClientDto(), m.getValue()))
                .toList();
    }
}
