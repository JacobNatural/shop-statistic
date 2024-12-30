package com.app.persistence.repository;

import com.app.persistence.entity.view.AgeAndMostProductProjection;
import com.app.persistence.entity.ClientEntity;
import com.app.persistence.entity.OrderEntity;
import com.app.persistence.entity.view.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link OrderEntity} entities in the persistence layer.
 * <p>
 * This interface extends the {@link CrudRepository} and provides additional custom query methods for retrieving orders and related data,
 * such as clients with the highest total payment, most common product categories, and clients with debit balances.
 * </p>
 */
public interface OrderRepository extends CrudRepository<OrderEntity> {

    /**
     * Retrieves a list of clients who have made the highest total payment.
     * <p>
     * The query calculates the total price of products purchased by each client and returns the clients with the highest total payment.
     * </p>
     *
     * @return a list of {@link ClientEntity} objects representing the clients with the highest total payment
     */
    @Query("""
            select o.clientEntity
                        from OrderEntity o
                        group by o.clientEntity
                        having sum(o.productEntity.price) =
                        (select max(totalPrice)
                        from( select sum(o.productEntity.price) as totalPrice from OrderEntity o group by o.clientEntity))""")
    List<ClientEntity> getClientWithBiggerPayment();


    /**
     * Retrieves a list of clients who have made the highest total payment in a specific product category.
     * <p>
     * The query calculates the total price of products purchased by each client within a specific category and returns
     * the clients with the highest total payment within that category.
     * </p>
     *
     * @param category the product category to filter by
     * @return a list of {@link ClientEntity} objects representing the clients with the highest total payment in the specified category
     */
    @Query("""
            select o.clientEntity
                                    from OrderEntity o
                                    where o.productEntity.category = :category
                                    group by o.clientEntity
                                    having sum(o.productEntity.price) =
                                    (select max(totalPrice)
                                    from(\s
                                    select sum(o2.productEntity.price) as totalPrice
                                    from OrderEntity o2
                                    where o2.productEntity.category = :category
                                    group by o2.clientEntity)
                                    )""")
    List<ClientEntity> getClientWithBiggerPaymentInCategory(String category);

    /**
     * Retrieves the most common product category for each age group.
     * <p>
     * The query groups the orders by client age and product category, and selects the category that occurs the most frequently
     * for each age group.
     * </p>
     *
     * @return a list of {@link AgeMostOftenCategoryProjection} containing the most common product category for each age group
     */
    @Query(value = """
            select c.age as age, p.category as category
            from orders o
            join clients c on o.client_id = c.id
            join products p on o.product_id = p.id
            group by c.age, p.category
            having count(p.category) = (
                                    select max(amount)
                                    from (select count(p2.category) as amount
                                    from orders o2
                                    join clients c2 on o2.client_id = c2.id
                                    join products p2 on o2.product_id = p2.id
                                    where c2.age = c.age
                                    group by p2.category)
                                    as total);
            """,
            nativeQuery = true)
    List<AgeMostOftenCategoryProjection> getAgeAndMostCategory();

    /**
     * Retrieves the clients who have purchased the most items within the same product category.
     * <p>
     * The query groups the orders by product category and client, and selects the clients who purchased the most items in a given category.
     * </p>
     *
     * @return a list of {@link CategoryAndMostClientProjection} representing the clients who purchased the most items within a product category
     */
    @Query(value = """
            select p.category as category,
            c.id as id,
            c.name as name,
            c.surname as surname,
            c.age as age,
            c.cash as cash
            from orders o
            join clients c on c.id = o.client_id
            join products p on p.id = o.product_id
            group by p.category, c.id
            having count(p.category) = (select max(totalCount)
            from (select count(p2.category) as totalCount
                    from orders o2
                    join clients c2 on c2.id = o2.client_id
                    join products p2 on p2.id = o2.product_id
                    where p.category = p2.category
                    group by c2.id, p2.category) as maxCounts);""",
            nativeQuery = true)
    List<CategoryAndMostClientProjection> getCategoryAndMostClient();

    /**
     * Retrieves a list of clients who have a debit balance (i.e., clients whose total spending exceeds their available cash).
     * <p>
     * The query calculates the total amount spent by each client and compares it with their available cash, returning clients with negative balances.
     * </p>
     *
     * @return a list of {@link ClientAndDebitDto} objects representing clients with a debit balance
     */
    @Query(value = """
            select new com.app.persistence.entity.view.ClientAndDebitDto(
            o.clientEntity.id,
            o.clientEntity.name,
            o.clientEntity.surname,
            o.clientEntity.age,
            o.clientEntity.cash,
            o.clientEntity.cash - sum(o.productEntity.price)
            ) from OrderEntity o
            group by o.clientEntity.id, o.clientEntity.name, o.clientEntity.surname, o.clientEntity.age, o.clientEntity.cash
            having o.clientEntity.cash - sum(o.productEntity.price) < 0
            """,
            nativeQuery = false)
    List<ClientAndDebitDto> getClientAndDebit();

    /**
     * Retrieves the most frequently purchased products for each client age group.
     * <p>
     * The query groups the orders by client age and product ID, selecting the product that was purchased the most frequently by clients of each age.
     * </p>
     *
     * @return a list of {@link AgeAndMostProductProjection} containing the most frequently purchased products for each age group
     */
    @Query(value = """
            select c.age as age,
                   p.id as id,
                   p.name as name,
                   p.category as category,
                   p.price as price from orders o
                                        join clients c on c.id = o.client_id
                                        join products p on p.id = o.product_id
            group by c.age, p.id
            having count(p.id) = (select max(count) from (
                select count(p2.id) as count
                from orders o2
                         join clients c2 on c2.id = o2.client_id
                         join products p2 on p2.id = o2.product_id
                where c.age = c2.age
                group by c2.age, p2.id) as sec)""", nativeQuery = true)
    List<AgeAndMostProductProjection> getAgeAndMostProduct();
}
