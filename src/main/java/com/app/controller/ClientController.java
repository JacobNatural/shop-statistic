package com.app.controller;

import com.app.controller.dto.ClientDto;
import com.app.controller.dto.ResponseDto;
import com.app.converter.many.ClientsConverter;
import com.app.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that manages client-related operations such as adding, retrieving, and removing clients.
 * <p>
 * This class handles HTTP requests related to clients, using the {@link ClientService} and {@link ClientsConverter}
 * to process client data. It exposes endpoints to add single or multiple clients, find clients by ID, and delete clients.
 * </p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    /**
     * Service responsible for client-related business logic.
     */
    private final ClientService clientService;

    /**
     * Converter used to convert clients between entities and DTOs.
     */
    private final ClientsConverter clientsConverter;

    /**
     * Endpoint to add a single client.
     * <p>
     * Accepts a {@link ClientDto} in the request body and returns the ID of the newly created client.
     * </p>
     *
     * @param clientDto the data of the client to be added.
     * @return a {@link ResponseDto} containing the ID of the added client.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> addClient(@RequestBody ClientDto clientDto) {
        return new ResponseDto<>(clientService.addClient(clientDto));
    }

    /**
     * Endpoint to add multiple clients.
     * <p>
     * Accepts a list of {@link ClientDto}s in the request body and returns a list of IDs of the newly created clients.
     * </p>
     *
     * @param clientsDto the list of clients to be added.
     * @return a {@link ResponseDto} containing the list of IDs of the added clients.
     */
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<Long>> addClients(@RequestBody List<ClientDto> clientsDto) {
        return new ResponseDto<>(clientService.addClients(clientsDto));
    }

    /**
     * Endpoint to retrieve a client by ID.
     * <p>
     * Returns a {@link ResponseDto} containing the client's data as a {@link ClientDto}.
     * </p>
     *
     * @param id the ID of the client to be retrieved.
     * @return a {@link ResponseDto} containing the client data.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<ClientDto> findById(@PathVariable Long id) {
        return new ResponseDto<>(clientService.findById(id).toClientDto());
    }

    /**
     * Endpoint to retrieve clients by a list of IDs.
     * <p>
     * Returns a list of {@link ClientDto} for the clients with the specified IDs.
     * </p>
     *
     * @param ids the list of client IDs to be retrieved.
     * @return a {@link ResponseDto} containing a list of client data.
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<ClientDto>> findAllByIds(@RequestParam List<Long> ids) {
        return new ResponseDto<>(clientsConverter.toDtoList(clientService.findAllByIds(ids)));
    }

    /**
     * Endpoint to remove a client by ID.
     * <p>
     * Removes the client with the specified ID and returns the ID of the removed client.
     * </p>
     *
     * @param id the ID of the client to be removed.
     * @return a {@link ResponseDto} containing the ID of the removed client.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> removeById(@PathVariable Long id) {
        return new ResponseDto<>(clientService.removeElement(id));
    }

    /**
     * Endpoint to remove multiple clients by IDs.
     * <p>
     * Accepts a list of client IDs to remove and returns a list of IDs of the removed clients.
     * </p>
     *
     * @param ids the list of client IDs to be removed.
     * @return a {@link ResponseDto} containing a list of IDs of the removed clients.
     */
    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN and LEADER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<Long>> removeAllByIds(@RequestParam List<Long> ids) {
        return new ResponseDto<>(clientService.removeAllByIds(ids));
    }
}
