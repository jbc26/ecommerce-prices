package com.inditex.ecommerceprices.infrastructure.inbound.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Product price result")
public record ProductPriceResponseDto(
    @Schema(description = "Product identifier", example = "35455")
    Integer productId,
    @Schema(description = "Brand identifier", example = "1")
    Integer brandId,
    @Schema(description = "Current price for the product", example = "10.32")
    BigDecimal price,
    @Schema(description = "Currency of the product price", example = "EUR")
    String currency,
    @Schema(description = "Start date when the product price is applied", example = "2020-06-15T16:00:00")
    LocalDateTime startDate,
    @Schema(description = "End date when the product price is applied", example = "2020-12-31T23:59:59")
    LocalDateTime endDate
) {

}
