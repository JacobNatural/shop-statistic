package com.app.controller;

import com.app.controller.dto.*;
import com.app.controller.dto.product.ProductDto;
import com.app.controller.dto.order.CategoryAndPriceStatisticDto;
import com.app.controller.dto.order.ClientAndDebitDto;
import com.app.controller.dto.order.GroupByDto;
import com.app.converter.many.impl.ClientsConverterImpl;
import com.app.converter.many.impl.ProductsConverterImpl;
import com.app.converter.many.impl.ShopConverterImpl;
import com.app.service.ShopStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling shop-related statistics and data aggregation.
 * <p>
 * This class provides endpoints for fetching shop-related statistics such as
 * top clients based on payment, most popular categories, product pricing
 * statistics, client demographics, and debt information.
 * </p>
 */
@RequiredArgsConstructor
@RequestMapping("/shop")
@RestController
public class ShopController {

    private final ShopStatisticService shopStatisticService;
    private final ClientsConverterImpl clientsConverter;
    private final ShopConverterImpl shopConverterImpl;
    private final ProductsConverterImpl productsConverterImpl;

    /**
     * Retrieves the clients with the biggest payments.
     * <p>
     * This endpoint returns a list of clients who have made the largest payments.
     * </p>
     *
     * @return a {@link ResponseDto} containing a list of {@link ClientDto} representing the clients with the largest payments.
     */
    @GetMapping("/clients/top")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<ClientDto>> getClientWithBiggerPayment() {
        return new ResponseDto<>(clientsConverter
                .toDtoList(shopStatisticService.getClientWithBiggerPayment()));
    }

    /**
     * Retrieves the clients with the biggest payments in a specific category.
     * <p>
     * This endpoint returns a list of clients who have made the largest payments
     * in a given product category.
     * </p>
     *
     * @param category the product category for which to find clients with the largest payments.
     * @return a {@link ResponseDto} containing a list of {@link ClientDto} representing the clients with the largest payments in the given category.
     */
    @GetMapping("/clients/top/{category}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<ClientDto>> getClientWithBiggerPaymentInCategory(@PathVariable String category) {
        return new ResponseDto<>(clientsConverter
                .toDtoList(shopStatisticService.getClientWithBiggerPaymentInCategory(category)));
    }

    /**
     * Retrieves the most popular product category for each age group.
     * <p>
     * This endpoint returns a list of age groups and the most popular product category
     * for each age group.
     * </p>
     *
     * @return a {@link ResponseDto} containing a list of {@link GroupByDto} representing age groups and their most popular categories.
     */
    @GetMapping("/clients/age/category")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<GroupByDto<Integer, String>>> getAgeAndMostCategory() {
        return new ResponseDto<>(shopConverterImpl.toAgeMostCategoryDto(shopStatisticService.getAgeAndMostCategory()));
    }

    /**
     * Retrieves category-wise product price statistics.
     * <p>
     * This endpoint returns the price statistics for products categorized by their product type.
     * </p>
     *
     * @return a {@link ResponseDto} containing a list of {@link CategoryAndPriceStatisticDto} representing product category price statistics.
     */
    @GetMapping("/products/category/price_statistic")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<CategoryAndPriceStatisticDto>> getCategoryAndPriceStatistic() {
        return new ResponseDto<>(productsConverterImpl
                .categoryAndPriceStatisticDto(shopStatisticService.getCategoryAndPriceStatistic()));
    }

    /**
     * Retrieves the most popular product for each age group.
     * <p>
     * This endpoint returns a list of age groups and the most popular product purchased
     * by customers in each age group.
     * </p>
     *
     * @return a {@link ResponseDto} containing a list of {@link GroupByDto} representing age groups and their most popular products.
     */
    @GetMapping("clients/age/product")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<GroupByDto<Integer, ProductDto>>> getAgeAndMostProduct() {
        return new ResponseDto<>(productsConverterImpl
                .ageAndMostProductDto(shopStatisticService.getAgeAndMostProduct()));
    }

    /**
     * Retrieves the most common clients in each product category.
     * <p>
     * This endpoint returns a list of product categories and the most common clients
     * within each category.
     * </p>
     *
     * @return a {@link ResponseDto} containing a list of {@link GroupByDto} representing product categories and their most common clients.
     */
    @GetMapping("/clients/category")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<GroupByDto<String, ClientDto>>> getCategoryAndMostClient() {
        return new ResponseDto<>(clientsConverter
                .toCategoryAndMostClientDto(shopStatisticService.getCategoryAndMostClient()));
    }

    /**
     * Retrieves clients and their associated debts.
     * <p>
     * This endpoint returns a list of clients along with their corresponding debit amounts.
     * </p>
     *
     * @return a {@link ResponseDto} containing a list of {@link ClientAndDebitDto} representing clients and their debts.
     */
    @GetMapping("/clients/debits")
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<List<ClientAndDebitDto>> getClientsAndDebt() {
        return new ResponseDto<>(clientsConverter
                .toClientAndDebitDto(shopStatisticService.getClientsAndDebit()));
    }
}
