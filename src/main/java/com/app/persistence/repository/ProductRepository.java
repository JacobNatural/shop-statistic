package com.app.persistence.repository;

import com.app.persistence.entity.ProductEntity;
import com.app.persistence.entity.view.PriceStatisticByCategoryProjection;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link ProductEntity} entities in the persistence layer.
 * <p>
 * This interface extends {@link CrudRepository} and {@link JpaSpecificationExecutor} to provide CRUD functionality,
 * as well as advanced querying capabilities such as filtering with specifications, retrieving products by category, and
 * performing aggregate statistics on products by category.
 * </p>
 */
public interface ProductRepository extends CrudRepository<ProductEntity>, JpaSpecificationExecutor<ProductEntity> {

    /**
     * Retrieves all products that match the given specification.
     * <p>
     * This method allows for dynamic filtering of products based on various criteria, using the {@link Specification}.
     * </p>
     *
     * @param spec the specification used for filtering products
     * @return a list of {@link ProductEntity} objects that match the specification
     */
    @NonNull
    List<ProductEntity> findAll(Specification<ProductEntity> spec);

    /**
     * Retrieves all products that belong to the specified category.
     *
     * @param category the product category to filter by
     * @return a list of {@link ProductEntity} objects belonging to the specified category
     */
    List<ProductEntity> findByCategory(String category);

    /**
     * Retrieves a product by its name and category.
     * <p>
     * If a product with the given name and category exists, it will be returned; otherwise, an empty {@link Optional} is returned.
     * </p>
     *
     * @param name     the name of the product
     * @param category the category of the product
     * @return an {@link Optional} containing the {@link ProductEntity} if found, otherwise an empty {@link Optional}
     */
    Optional<ProductEntity> findByNameAndCategory(String name, String category);

    /**
     * Retrieves a list of all unique product categories in the repository.
     * <p>
     * This query returns a list of distinct product categories that are available in the database.
     * </p>
     *
     * @return a list of strings representing all unique product categories
     */
    @Query("""
            select p.category from ProductEntity p
                            group by p.category""")
    List<String> getAllCategories();

    /**
     * Retrieves the price statistics (average, minimum, and maximum price) for each product category,
     * along with the product that has the minimum price and the product that has the maximum price in each category.
     * <p>
     * This query calculates aggregate statistics for each category, including the average price, minimum price, and maximum price.
     * It also retrieves the product details for the products with the minimum and maximum prices.
     * </p>
     *
     * @return a list of {@link PriceStatisticByCategoryProjection} containing the statistics for each category
     */
    @Query(value = """
            WITH     category_stats AS (
                                                    SELECT
                                                        p.category AS category,
                                                        AVG(p.price) AS avg_price,
                                                        MIN(p.price) AS min_price,
                                                        MAX(p.price) AS max_price
                                                    FROM products p
                                                    GROUP BY p.category
                                                )
                                                SELECT
                                                    cs.category as category,
                                                    cs.avg_price as avgPrice,
                                                    p_min.id AS minId, p_min.name AS minName, p_min.price AS minPrice,
                                                    p_max.id AS maxId, p_max.name AS maxName, p_max.price AS maxPrice
                                                FROM category_stats cs
                                                         JOIN products p_min ON p_min.price = cs.min_price AND p_min.category = cs.category
                                                         JOIN products p_max ON p_max.price = cs.max_price AND p_max.category = cs.category;""",
            nativeQuery = true)
    List<PriceStatisticByCategoryProjection> getCategoryAndPriceStatistic();
}
