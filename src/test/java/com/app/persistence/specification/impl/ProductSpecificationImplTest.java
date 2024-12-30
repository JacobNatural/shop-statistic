package com.app.persistence.specification.impl;

import com.app.persistence.entity.ProductEntity;
import com.app.persistence.repository.specification.impl.ProductSpecificationImpl;
import com.app.persistence.repository.specification.specification.ProductFilterSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductSpecificationImplTest {

    @Mock
    Root<ProductEntity> root;

    @Mock
    CriteriaBuilder cb;

    @Mock
    Predicate initialPredicate;

    @Mock
    Predicate likePredicate;

    ProductSpecificationImpl productSpecification;


    @BeforeEach
    public void setUp() {
        productSpecification = new ProductSpecificationImpl();
    }


    @Test
    @DisplayName("When filtering by category, verify that the correct method (cb.like) is invoked")
    public void test1() {

        ProductFilterSpecification filterSpecification = new ProductFilterSpecification(null, null, "Electronics");

        when(cb.conjunction()).thenReturn(initialPredicate);
        when(cb.like(root.get("category"), "Electronics")).thenReturn(likePredicate);
        when(cb.and(initialPredicate, likePredicate)).thenReturn(likePredicate);

        Specification<ProductEntity> specification = productSpecification.dynamicFilter(filterSpecification);
        Predicate resultPredicate = specification.toPredicate(root, null, cb);

        assertNotNull(resultPredicate);

        Mockito.verify(cb, times(1))
                .like(root.get("category"), "Electronics");
        Mockito.verify(cb, times(1))
                .and(initialPredicate, likePredicate);
    }

    @Test
    @DisplayName("When filtering by low price, verify that the correct method (cb.greaterThanOrEqualTo) is invoked")
    public void test2() {

        ProductFilterSpecification filterSpecification = new ProductFilterSpecification(BigDecimal.ONE, null, null);

        when(cb.conjunction()).thenReturn(initialPredicate);
        when(cb.greaterThanOrEqualTo(root.get("lowPrice"), BigDecimal.ONE)).thenReturn(likePredicate);
        when(cb.and(initialPredicate, likePredicate)).thenReturn(likePredicate);

        Specification<ProductEntity> specification = productSpecification.dynamicFilter(filterSpecification);
        Predicate resultPredicate = specification.toPredicate(root, null, cb);

        assertNotNull(resultPredicate);

        Mockito.verify(cb, times(1))
                .greaterThanOrEqualTo(root.get("lowPrice"), BigDecimal.ONE);
        Mockito.verify(cb, times(1))
                .and(initialPredicate, likePredicate);
    }

    @Test
    @DisplayName("When filtering by high price, verify that the correct method (cb.lessThanOrEqualTo) is invoked")
    public void test3() {

        ProductFilterSpecification filterSpecification = new ProductFilterSpecification(null, BigDecimal.ONE, "");

        when(cb.conjunction()).thenReturn(initialPredicate);
        when(cb.lessThanOrEqualTo(root.get("highPrice"), BigDecimal.ONE)).thenReturn(likePredicate);
        when(cb.and(initialPredicate, likePredicate)).thenReturn(likePredicate);

        Specification<ProductEntity> specification = productSpecification.dynamicFilter(filterSpecification);
        Predicate resultPredicate = specification.toPredicate(root, null, cb);

        assertNotNull(resultPredicate);

        Mockito.verify(cb, times(1))
                .lessThanOrEqualTo(root.get("highPrice"), BigDecimal.ONE);
        Mockito.verify(cb, times(1))
                .and(initialPredicate, likePredicate);
    }
}
