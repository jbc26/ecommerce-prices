package com.inditex.ecommerceprices.domain.entity;

import java.time.LocalDateTime;
import org.javamoney.moneta.Money;

public record ProductPrice(
    Integer priceList,
    Integer productId,
    Integer brandId,
    Money price,
    Integer priority,
    LocalDateTime startDate,
    LocalDateTime endDate) {

}