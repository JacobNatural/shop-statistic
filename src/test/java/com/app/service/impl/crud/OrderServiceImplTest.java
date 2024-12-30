package com.app.service.impl.crud;

import com.app.controller.dto.order.OrderAddDto;
import com.app.controller.dto.order.OrdersAddDto;
import com.app.converter.many.OrdersConverter;
import com.app.converter.single.Converter;
import com.app.model.Order;
import com.app.persistence.entity.OrderEntity;
import com.app.persistence.repository.ClientRepository;
import com.app.persistence.repository.OrderRepository;
import com.app.persistence.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.app.data.ClientData.CLIENT_ENTITY_READ_1;
import static com.app.data.OrderData.ORDER_ENTITY1;
import static com.app.data.OrderData.ORDER_ENTITY2;
import static com.app.data.ProductData.PRODUCT_ENTITY_READ1;
import static com.app.data.ProductData.PRODUCT_ENTITY_READ2;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Converter<OrderEntity, Order> converter;

    @Mock
    private OrdersConverter ordersConverter;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl service;

    @Test
    @DisplayName("When adding an order, and the client does not exist, throw an EntityNotFoundException.")
    public void test1() {

        var orderDto = new OrderAddDto(1L, 1L);

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.addOrder(orderDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Client not found");

        Mockito.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("When adding an order, and the product does not exist, throw an EntityNotFoundException.")
    public void test2() {

        var orderDto = new OrderAddDto(1L, 1L);

        var inOrder = Mockito.inOrder(clientRepository, productRepository);

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

        Mockito.when(productRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.addOrder(orderDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Product not found");

        inOrder.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(productRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("When adding an order, return the order ID upon successful completion.")
    public void test3() {

        var orderDto = new OrderAddDto(1L, 1L);

        var inOrder = Mockito.inOrder(clientRepository, productRepository, orderRepository);

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

        Mockito.when(productRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PRODUCT_ENTITY_READ1));

        Mockito.when(orderRepository.save(ArgumentMatchers.any(OrderEntity.class)))
                .thenReturn(ORDER_ENTITY1);

        Assertions.assertThat(service.addOrder(orderDto))
                .isEqualTo(1L);

        inOrder.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(productRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(orderRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(OrderEntity.class));
    }

    @Test
    @DisplayName("When adding an orders and the client does not exist, throw an EntityNotFoundException.")
    public void test4() {

        var ordersDto = new OrdersAddDto(1L, List.of(1L, 2L));

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.addOrders(ordersDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Client not found");

        Mockito.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("When adding an orders and the one of product does not exist, throw an EntityNotFoundException.")
    public void test5() {

        var ordersDto = new OrdersAddDto(1L, List.of(1L, 2L));

        var inOrder = Mockito.inOrder(clientRepository, productRepository);

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

        Mockito.when(productRepository.findAllById(ArgumentMatchers.anyList()))
                .thenReturn(List.of(PRODUCT_ENTITY_READ1));

        Assertions.assertThatThrownBy(() -> service.addOrders(ordersDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Not all products were found");

        Mockito.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(productRepository, Mockito.times(1))
                .findAllById(ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("When adding an orders, return the orders ID upon successful completion.")
    public void test6() {

        var ordersDto = new OrdersAddDto(1L, List.of(1L, 2L));

        var inOrder = Mockito.inOrder(clientRepository, productRepository, orderRepository);

        Mockito.when(clientRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CLIENT_ENTITY_READ_1));

        Mockito.when(productRepository.findAllById(ArgumentMatchers.anyList()))
                .thenReturn(List.of(PRODUCT_ENTITY_READ1, PRODUCT_ENTITY_READ2));

        Mockito.when(orderRepository.saveAll(ArgumentMatchers.anyList()))
                .thenReturn(List.of(ORDER_ENTITY1, ORDER_ENTITY2));

        Assertions.assertThat(service.addOrders(ordersDto))
                .isEqualTo(List.of(1L, 2L));

        Mockito.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(clientRepository, Mockito.times(1))
                .findById(ArgumentMatchers.anyLong());

        inOrder.verify(productRepository, Mockito.times(1))
                .findAllById(ArgumentMatchers.anyList());

        inOrder.verify(orderRepository, Mockito.times(1))
                .saveAll(ArgumentMatchers.anyList());

    }
}