package com.inditex.ecommerceprices.infrastructure.outbound.repository.jpa;

import com.inditex.ecommerceprices.infrastructure.outbound.repository.dao.ProductPriceDao;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductPriceJpaRepository extends JpaRepository<ProductPriceDao, Integer> {

    @Query(value = """
            SELECT * FROM PRICES
            WHERE PRODUCT_ID = :productId
              AND BRAND_ID = :brandId
              AND :applicationDate BETWEEN START_DATE AND END_DATE
            ORDER BY PRIORITY DESC
            LIMIT 1
        """, nativeQuery = true)
    Optional<ProductPriceDao> filterPrice(
        @Param("productId") Integer productId,
        @Param("brandId") Integer brandId,
        @Param("applicationDate") LocalDateTime applicationDate);
}
