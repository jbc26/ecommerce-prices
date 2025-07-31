package com.inditex.ecommerceprices.domain.entity.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductPriceNotFoundException extends Exception {

    public ProductPriceNotFoundException(Integer productId, Integer brandId,
        LocalDateTime applicationDate) {
        super(String.format(
            "Product price not found with params productId=%d, brandId=%d and applicationDate=%s",
            productId, brandId, applicationDate.format(DateTimeFormatter.ISO_DATE_TIME)));
    }
}
