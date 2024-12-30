package com.app.service;

import com.app.controller.dto.ClientDto;
import com.app.model.Client;

import java.util.List;

/**
 * Interface providing methods for managing clients. Extends the {@link CrudService} interface to inherit basic CRUD operations
 * for {@link Client} entities. In addition to the basic CRUD operations, this service provides methods for adding a single
 * client or a list of clients.
 */
public interface ClientService extends CrudService<Client> {

    /**
     * Adds a new client.
     *
     * @param clientDto the DTO containing the client data to be added
     * @return the ID of the newly added client
     */
    Long addClient(ClientDto clientDto);

    /**
     * Adds multiple clients.
     *
     * @param clientsDto a list of DTOs containing the client data to be added
     * @return a list of IDs of the newly added clients
     */
    List<Long> addClients(List<ClientDto> clientsDto);
}
