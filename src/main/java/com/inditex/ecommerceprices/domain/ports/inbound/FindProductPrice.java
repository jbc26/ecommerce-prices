package com.inditex.ecommerceprices.domain.ports.inbound;

import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import java.time.LocalDateTime;
import reactor.core.publisher.Mono;

public interface FindProductPrice {

    /**
     * Find the product price filtered by input parameters
     *
     * @param productId       Product ID
     * @param brandId         Brand ID
     * @param applicationDate Datetime when the query is requested
     * @return ProductPrice or ProductPriceNotFoundException when cannot retrieve the product price
     * entity
     */
    Mono<ProductPrice> find(Integer productId, Integer brandId, LocalDateTime applicationDate)
        throws ProductPriceNotFoundException;
}
