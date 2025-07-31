package com.inditex.ecommerceprices.infrastructure.inbound.controller.dto;

import org.springframework.http.HttpStatus;

public record ProductPriceErrorResponseDto(
    String message,
    HttpStatus status
) {

}
