package com.app.controller;

import com.app.controller.dto.order.OrderAddDto;
import com.app.controller.dto.order.OrderFindDto;
import com.app.controller.dto.order.OrdersAddDto;
import com.app.controller.dto.ResponseDto;
import com.app.converter.many.OrdersConverter;
import com.app.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing orders in the application.
 * <p>
 * This class provides endpoints for creating, retrieving, and deleting orders.
 * </p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrdersConverter ordersConverter;

    /**
     * Creates a new order.
     * <p>
     * This method accepts an {@link OrderAddDto} object in the request body and creates a new order.
     * It returns the ID of the newly created order wrapped in a {@link ResponseDto}.
     * </p>
     *
     * @param orderAddDto the order data to be added.
     * @return a {@link ResponseDto} containing the ID of the created order.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> addOrder(@RequestBody OrderAddDto orderAddDto) {
        return new ResponseDto<>(orderService
                .addOrder(orderAddDto));
    }

    /**
     * Creates multiple orders.
     * <p>
     * This method accepts a list of {@link OrdersAddDto} objects in the request body and creates multiple orders.
     * It returns the IDs of the newly created orders wrapped in a {@link ResponseDto}.
     * </p>
     *
     * @param orderDto the list of orders data to be added.
     * @return a {@link ResponseDto} containing the list of IDs of the created orders.
     */
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<Long>> addOrders(@RequestBody OrdersAddDto orderDto) {
        return new ResponseDto<>(orderService
                .addOrders(orderDto));
    }

    /**
     * Retrieves an order by its ID.
     * <p>
     * This method accepts the ID of the order as a path variable and returns the details of the order in a {@link OrderFindDto}.
     * </p>
     *
     * @param id the ID of the order to be retrieved.
     * @return a {@link ResponseDto} containing the details of the found order.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<OrderFindDto> findById(@PathVariable Long id) {
        return new ResponseDto<>(orderService
                .findById(id).toOrderFindDto());
    }

    /**
     * Retrieves multiple orders by their IDs.
     * <p>
     * This method accepts a list of order IDs as request parameters and returns the details of the orders in a list of {@link OrderFindDto}.
     * </p>
     *
     * @param ids the list of order IDs to be retrieved.
     * @return a {@link ResponseDto} containing a list of found orders.
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<OrderFindDto>> findAllByIds(@RequestParam List<Long> ids) {
        return new ResponseDto<>(ordersConverter
                .toOrderFindDto(orderService.findAllByIds(ids)));
    }

    /**
     * Removes an order by its ID.
     * <p>
     * This method accepts the ID of the order to be removed as a path variable and deletes the order.
     * It returns the ID of the removed order wrapped in a {@link ResponseDto}.
     * </p>
     *
     * @param id the ID of the order to be removed.
     * @return a {@link ResponseDto} containing the ID of the removed order.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> removeById(@PathVariable Long id) {
        return new ResponseDto<>(orderService.removeElement(id));
    }

    /**
     * Removes multiple orders by their IDs.
     * <p>
     * This method accepts a list of order IDs as request parameters and deletes the orders.
     * It returns the list of IDs of the removed orders wrapped in a {@link ResponseDto}.
     * </p>
     *
     * @param ids the list of order IDs to be removed.
     * @return a {@link ResponseDto} containing a list of the IDs of the removed orders.
     */
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<Long>> removeAllByIds(@RequestParam List<Long> ids) {
        return new ResponseDto<>(orderService.removeAllByIds(ids));
    }
}
