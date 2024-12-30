package com.app.service;

import com.app.model.Client;
import com.app.model.Product;
import com.app.statistic.Statistic;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Interface for generating shop statistics related to clients, products, and categories.
 * This service provides methods for retrieving data about client payments, product categories,
 * and various statistical breakdowns such as age distribution, top products, and category statistics.
 */
public interface ShopStatisticService {

    /**
     * Retrieves a list of clients with the highest payments.
     *
     * @return a list of clients with the highest payments
     */
    List<Client> getClientWithBiggerPayment();

    /**
     * Retrieves a list of clients who have made the highest payments in a specific category.
     *
     * @param category the category of interest
     * @return a list of clients with the highest payments in the specified category
     */
    List<Client> getClientWithBiggerPaymentInCategory(String category);

    /**
     * Retrieves a map of age groups and their most purchased product categories.
     *
     * @return a map where the key is the age group and the value is a list of most purchased categories by that group
     */
    Map<Integer, List<String>> getAgeAndMostCategory();

    /**
     * Retrieves a map of age groups and their most purchased products.
     *
     * @return a map where the key is the age group and the value is a list of most purchased products by that group
     */
    Map<Integer, List<Product>> getAgeAndMostProduct();

    /**
     * Retrieves statistics on product prices for each category.
     *
     * @return a map where the key is the category name and the value is a {@link Statistic} object containing product price statistics
     */
    Map<String, Statistic<Product, BigDecimal>> getCategoryAndPriceStatistic();

    /**
     * Retrieves a map of product categories and the clients who have purchased the most from each category.
     *
     * @return a map where the key is the category name and the value is a list of clients who purchased the most from that category
     */
    Map<String, List<Client>> getCategoryAndMostClient();

    /**
     * Retrieves a map of clients and their associated debit amounts.
     *
     * @return a map where the key is a {@link Client} object and the value is their total debit amount
     */
    Map<Client, BigDecimal> getClientsAndDebit();
}
