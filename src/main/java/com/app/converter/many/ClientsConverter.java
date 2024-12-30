package com.app.converter.many;

import com.app.controller.dto.ClientDto;
import com.app.controller.dto.order.ClientAndDebitDto;
import com.app.controller.dto.order.GroupByDto;
import com.app.model.Client;
import com.app.persistence.entity.ClientEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Interface for converting between various representations of {@link Client} entities
 * and related Data Transfer Objects (DTOs).
 * <p>
 * This interface provides methods to convert clients between domain models (such as {@link Client} and {@link ClientEntity})
 * and their corresponding DTOs used in API responses and requests. It also handles transformations for statistical
 * and grouped data related to clients.
 * </p>
 */
public interface ClientsConverter {

    /**
     * Converts a list of {@link Client} objects into a list of {@link ClientDto} objects.
     *
     * @param clients the list of {@link Client} objects to convert
     * @return a list of {@link ClientDto} objects
     */
    List<ClientDto> toDtoList(List<Client> clients);

    /**
     * Converts a list of {@link ClientDto} objects into a list of {@link ClientEntity} objects.
     *
     * @param dtos the list of {@link ClientDto} objects to convert
     * @return a list of {@link ClientEntity} objects
     */
    List<ClientEntity> toEntityList(List<ClientDto> dtos);

    /**
     * Converts a map of client categories to their most common clients into a list of {@link GroupByDto}
     * objects, where each entry contains a category as a string and a list of {@link ClientDto} objects.
     *
     * @param map a map where the key is a category (String) and the value is a list of {@link Client} objects
     * @return a list of {@link GroupByDto} objects, each containing a category and its associated list of clients
     */
    List<GroupByDto<String, ClientDto>> toCategoryAndMostClientDto(Map<String, List<Client>> map);

    /**
     * Converts a map of clients and their corresponding debit amounts into a list of {@link ClientAndDebitDto} objects.
     *
     * @param map a map where the key is a {@link Client} and the value is their corresponding debit amount (BigDecimal)
     * @return a list of {@link ClientAndDebitDto} objects, each containing a {@link ClientDto} and a debit amount
     */
    List<ClientAndDebitDto> toClientAndDebitDto(Map<Client, BigDecimal> map);
}
