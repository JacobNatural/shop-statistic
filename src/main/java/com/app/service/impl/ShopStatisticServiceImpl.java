package com.app.service.impl;

import com.app.model.Client;
import com.app.persistence.entity.ClientEntity;
import com.app.persistence.entity.view.*;
import com.app.model.Product;
import com.app.persistence.repository.OrderRepository;
import com.app.persistence.repository.ProductRepository;
import com.app.service.ShopStatisticService;
import com.app.statistic.Statistic;
import com.app.statistic.impl.StatisticImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ShopStatisticService} interface that provides functionality for fetching and processing
 * statistics related to clients, products, and orders.
 * <p>
 * This service uses repositories for orders and products to gather statistical data about customer behavior, product sales,
 * and spending patterns. The results are aggregated and returned in a variety of formats, such as maps, lists, and statistics.
 * </p>
 */
@Transactional
@Service
@AllArgsConstructor
public class ShopStatisticServiceImpl implements ShopStatisticService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * Retrieves a list of clients with the highest total payment amounts.
     * <p>
     * This method queries the {@link OrderRepository} to find clients who have spent the most, and then maps the result
     * to a list of {@link Client} objects.
     * </p>
     *
     * @return A list of clients with the highest total payment amounts.
     */
    @Override
    public List<Client> getClientWithBiggerPayment() {
        return orderRepository
                .getClientWithBiggerPayment()
                .stream()
                .map(ClientEntity::toClient)
                .toList();
    }

    /**
     * Retrieves a list of clients who have spent the most in a specific product category.
     * <p>
     * This method allows filtering by category and uses the {@link OrderRepository} to find clients who have spent the most
     * in the specified category, returning the result as a list of {@link Client} objects.
     * </p>
     *
     * @param category The category to filter by.
     * @return A list of clients who have spent the most in the specified category.
     * @throws IllegalArgumentException if the category is null or empty.
     */
    @Override
    public List<Client> getClientWithBiggerPaymentInCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }

        return orderRepository
                .getClientWithBiggerPaymentInCategory(category)
                .stream()
                .map(ClientEntity::toClient)
                .toList();
    }

    /**
     * Retrieves a map of client ages and their most frequently purchased product categories.
     * <p>
     * This method queries the {@link OrderRepository} to fetch the age of clients along with their most purchased product category.
     * The result is returned as a map where the keys are client ages, and the values are lists of categories.
     * </p>
     *
     * @return A map of client ages to lists of most frequently purchased categories.
     */
    @Override
    public Map<Integer, List<String>> getAgeAndMostCategory() {
        return orderRepository
                .getAgeAndMostCategory()
                .stream()
                .collect(Collectors.toMap(
                        AgeMostOftenCategoryProjection::getAge,
                        view -> new ArrayList<>(List.of(view.getCategory())),
                        (v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        })
                );
    }

    /**
     * Retrieves a map of client ages and their most frequently purchased products.
     * <p>
     * This method queries the {@link OrderRepository} to fetch client ages along with their most purchased products.
     * The result is returned as a map where the keys are client ages, and the values are lists of {@link Product} objects.
     * </p>
     *
     * @return A map of client ages to lists of most frequently purchased products.
     */
    @Override
    public Map<Integer, List<Product>> getAgeAndMostProduct() {
        return orderRepository.getAgeAndMostProduct()
                .stream()
                .collect(Collectors.toMap(
                        AgeAndMostProductProjection::getAge,
                        view -> new ArrayList<>(List.of(new Product(
                                view.getId(), view.getName(), view.getCategory(), view.getPrice()))),
                        (v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }
                ));
    }

    /**
     * Retrieves statistics about product prices grouped by category.
     * <p>
     * This method uses the {@link ProductRepository} to get statistics about product prices within each category.
     * The statistics are returned as a map of category names to {@link Statistic} objects that contain the price statistics.
     * </p>
     *
     * @return A map of categories to product price statistics.
     */
    @Override
    public Map<String, Statistic<Product, BigDecimal>> getCategoryAndPriceStatistic() {
        return productRepository
                .getCategoryAndPriceStatistic()
                .stream()
                .collect(
                        Collectors.toMap(
                                PriceStatisticByCategoryProjection::getCategory,
                                StatisticImpl::fromView,
                                Statistic::merge)
                );
    }

    /**
     * Retrieves a map of product categories and their most frequent clients.
     * <p>
     * This method queries the {@link OrderRepository} to fetch the most frequent clients for each product category.
     * The result is returned as a map where the keys are product categories, and the values are lists of {@link Client} objects.
     * </p>
     *
     * @return A map of product categories to lists of the most frequent clients.
     */
    @Override
    public Map<String, List<Client>> getCategoryAndMostClient() {
        return orderRepository.getCategoryAndMostClient()
                .stream()
                .collect(Collectors.toMap(
                        CategoryAndMostClientProjection::getCategory,
                        view -> new ArrayList<>(List.of(new Client(
                                view.getId(), view.getName(), view.getSurname(), view.getAge(), view.getCash()
                        ))),
                        (v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }
                ));
    }

    /**
     * Retrieves a map of clients and their associated debit values.
     * <p>
     * This method queries the {@link OrderRepository} to fetch each client along with their debit value, and returns the result
     * as a map of {@link Client} objects to their corresponding debit values.
     * </p>
     *
     * @return A map of clients to their debit values.
     */
    @Override
    public Map<Client, BigDecimal> getClientsAndDebit() {
        return orderRepository.getClientAndDebit()
                .stream()
                .collect(Collectors.toMap(
                        view -> new Client(
                                view.getId(), view.getName(), view.getSurname(), view.getAge(), view.getCash()
                        ),
                        ClientAndDebitDto::getDebit
                ));
    }
}
