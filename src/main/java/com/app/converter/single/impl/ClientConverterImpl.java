package com.app.converter.single.impl;

import com.app.converter.single.Converter;
import com.app.model.Client;
import com.app.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link Converter} interface for converting between {@link ClientEntity}
 * and {@link Client} objects.
 * <p>
 * This class provides the conversion logic for transforming a {@link ClientEntity} (representing a
 * database entity) to a {@link Client} (a business model) and vice versa.
 * </p>
 */
@Component
public class ClientConverterImpl implements Converter<ClientEntity, Client> {

    /**
     * Converts a {@link ClientEntity} to a {@link Client} model object.
     *
     * @param clientEntity the {@link ClientEntity} object to convert
     * @return the corresponding {@link Client} model
     */
    @Override
    public Client toModel(ClientEntity clientEntity) {
        return clientEntity.toClient();
    }

    /**
     * Converts a {@link Client} model object to a {@link ClientEntity}.
     *
     * @param client the {@link Client} model to convert
     * @return the corresponding {@link ClientEntity} object
     */
    @Override
    public ClientEntity toEntity(Client client) {
        return client.toClientEntity();
    }
}
