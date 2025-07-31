package com.inditex.ecommerceprices.domain.ports.outbound;

import com.inditex.ecommerceprices.domain.entity.ProductPrice;
import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import java.time.LocalDateTime;
import reactor.core.publisher.Mono;

public interface FindProductPriceRepository {

    /**
     * Retrieve ProductPrice filtered by input parameters
     *
     * @param productId           Identifier of the product
     * @param brandId             Identifier of the brand
     * @param applicationDateTime Date when the price is applied
     * @return ProductPrice that match the parameters or ProductPriceNotFoundException if not match
     * @throws com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException ProductPrice
     *                                                                                           not
     *                                                                                           found
     */
    Mono<ProductPrice> find(
        Integer productId,
        Integer brandId,
        LocalDateTime applicationDateTime
    ) throws ProductPriceNotFoundException;
}
