package com.app.service.impl.crud;

import com.app.controller.dto.ClientDto;
import com.app.converter.many.ClientsConverter;
import com.app.exception.ResourceAlreadyExistException;
import com.app.model.Client;
import com.app.persistence.entity.ClientEntity;
import com.app.persistence.repository.ClientRepository;
import com.app.converter.single.Converter;
import com.app.service.ClientService;
import com.app.validator.Validator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link ClientService} interface, providing business logic for managing clients.
 * <p>
 * This service provides methods for adding a single client or multiple clients to the system. It ensures that each client is unique
 * by checking for the existence of clients based on their name and surname before adding them to the repository.
 * </p>
 */
@Service
public class ClientServiceImpl extends GenericServiceImpl<ClientEntity, Client> implements ClientService {

    private final Validator<ClientDto> validator;
    private final ClientRepository clientRepository;
    private final ClientsConverter clientsConverter;

    /**
     * Constructs a new {@link ClientServiceImpl} with the specified dependencies.
     * <p>
     * This constructor initializes the required repository, validator, and converter for handling client data.
     * </p>
     *
     * @param repository       the {@link ClientRepository} to interact with the database
     * @param converter        the {@link Converter} to convert between {@link ClientEntity} and {@link Client}
     * @param validator        the {@link Validator} used to validate client DTOs
     * @param clientRepository the {@link ClientRepository} for checking if clients already exist
     * @param clientsConverter the {@link ClientsConverter} to convert a list of client DTOs into entities
     */
    public ClientServiceImpl(
            ClientRepository repository,
            Converter<ClientEntity, Client> converter,
            Validator<ClientDto> validator,
            ClientRepository clientRepository,
            ClientsConverter clientsConverter) {
        super(repository, converter);
        this.validator = validator;
        this.clientRepository = clientRepository;
        this.clientsConverter = clientsConverter;
    }

    /**
     * Adds a single client to the system.
     * <p>
     * This method validates the provided client DTO, checks if the client already exists based on their name and surname,
     * and if not, saves the client in the repository. If a client with the same name and surname exists, a {@link ResourceAlreadyExistException}
     * is thrown.
     * </p>
     *
     * @param clientDto the {@link ClientDto} object containing the client data to be added
     * @return the ID of the newly added client
     * @throws ResourceAlreadyExistException if a client with the same name and surname already exists in the system
     */
    public Long addClient(ClientDto clientDto) {
        // Validate client DTO
        Validator.validate(clientDto, validator);

        // Check if client already exists
        if (clientRepository.findByNameAndSurname(clientDto.name(), clientDto.surname()).isPresent()) {
            throw new ResourceAlreadyExistException("Client already exists");
        }

        // Save the client and return the ID
        return repository.save(clientDto.toClientEntity()).getId();
    }

    /**
     * Adds a list of clients to the system.
     * <p>
     * This method validates each client DTO, checks if each client already exists, and saves the list of clients
     * in the repository. If a client with the same name and surname exists, a {@link ResourceAlreadyExistException} is thrown
     * with the specific client's details.
     * </p>
     *
     * @param clientsDto a list of {@link ClientDto} objects containing the client data to be added
     * @return a list of IDs of the newly added clients
     * @throws ResourceAlreadyExistException if any client in the list already exists
     */
    public List<Long> addClients(List<ClientDto> clientsDto) {
        // Validate each client DTO and check for existing clients
        clientsDto.forEach(c -> {
            Validator.validate(c, validator);
            if (clientRepository.findByNameAndSurname(c.name(), c.surname()).isPresent()) {
                throw new ResourceAlreadyExistException("Client: %s already exists".formatted(c));
            }
        });

        // Save all clients and return their IDs
        return repository.saveAll(clientsConverter.toEntityList(clientsDto))
                .stream()
                .map(ClientEntity::getId)
                .toList();
    }
}
