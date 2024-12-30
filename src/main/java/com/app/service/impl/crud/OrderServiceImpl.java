package com.app.service.impl.crud;

import com.app.controller.dto.order.OrderAddDto;
import com.app.controller.dto.order.OrdersAddDto;
import com.app.converter.single.Converter;
import com.app.model.Order;
import com.app.persistence.entity.OrderEntity;
import com.app.persistence.repository.ClientRepository;
import com.app.persistence.repository.CrudRepository;
import com.app.persistence.repository.OrderRepository;
import com.app.persistence.repository.ProductRepository;
import com.app.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the {@link OrderService} interface that provides CRUD operations for managing orders.
 * This service class interacts with repositories for clients, products, and orders, and performs
 * necessary validations before saving orders.
 * <p>
 * The service methods ensure that clients and products are validated before an order is created.
 * </p>
 */
@Transactional
@Service
public class OrderServiceImpl extends GenericServiceImpl<OrderEntity, Order> implements OrderService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Constructor that initializes the {@link OrderServiceImpl} with necessary repositories and a converter.
     *
     * @param repository        the repository for {@link OrderEntity} used by the generic service
     * @param converter         the converter used to convert between {@link OrderEntity} and {@link Order}
     * @param clientRepository  the repository for {@link com.app.persistence.entity.ClientEntity} used for client lookup
     * @param orderRepository   the repository for {@link OrderEntity} used for order persistence
     * @param productRepository the repository for {@link com.app.persistence.entity.ProductEntity} used for product lookup
     */
    public OrderServiceImpl(
            CrudRepository<OrderEntity> repository,
            Converter<OrderEntity, Order> converter,
            ClientRepository clientRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        super(repository, converter);
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Adds a single order based on the provided {@link OrderAddDto}.
     * <p>
     * This method validates that the client and product exist, then creates and saves an order entity.
     * </p>
     *
     * @param orderAddDto the DTO containing information about the order to be added
     * @return the ID of the created order
     * @throws EntityNotFoundException if the client or product specified in the DTO is not found
     */
    public Long addOrder(OrderAddDto orderAddDto) {
        var clientEntity = clientRepository.findById(orderAddDto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        var productEntity = productRepository.findById(orderAddDto.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return orderRepository.save(OrderEntity
                .builder()
                .clientEntity(clientEntity)
                .productEntity(productEntity)
                .build()).getId();
    }

    /**
     * Adds multiple orders based on the provided {@link OrdersAddDto}.
     * <p>
     * This method validates that the client exists and that all products specified in the DTO are found in the database.
     * If any product is not found, an {@link EntityNotFoundException} is thrown.
     * </p>
     *
     * @param ordersAddDto the DTO containing information about multiple orders to be added
     * @return a list of IDs of the created orders
     * @throws EntityNotFoundException if the client or any of the products specified in the DTO are not found
     */
    public List<Long> addOrders(OrdersAddDto ordersAddDto) {

        var clientEntity = clientRepository
                .findById(ordersAddDto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        var productsEntity = productRepository.findAllById(ordersAddDto.productsId());

        if (productsEntity.size() != ordersAddDto.productsId().size()) {
            throw new EntityNotFoundException("Not all products were found");
        }

        return orderRepository.saveAll(ordersAddDto.orderEntityList(clientEntity, productsEntity))
                .stream()
                .map(OrderEntity::getId)
                .toList();
    }
}
