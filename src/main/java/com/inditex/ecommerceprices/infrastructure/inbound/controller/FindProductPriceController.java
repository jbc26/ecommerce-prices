package com.inditex.ecommerceprices.infrastructure.inbound.controller;

import com.inditex.ecommerceprices.application.product.FindProductPriceUseCase;
import com.inditex.ecommerceprices.domain.entity.exception.ProductPriceNotFoundException;
import com.inditex.ecommerceprices.infrastructure.inbound.controller.dto.ProductPriceResponseDto;
import com.inditex.ecommerceprices.infrastructure.inbound.controller.mapper.ProductPriceDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Tag(name = "Product price", description = "Product price operations")
public class FindProductPriceController {

    private final ProductPriceDtoMapper productPriceDtoMapper;
    private final FindProductPriceUseCase findProductPriceUseCase;

    @Operation(
        summary = "Get product price",
        description = "Retrieve product price that match the input parameters",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product price found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Product price not found"),
            @ApiResponse(responseCode = "500", description = "Internal error")
        }
    )
    @GetMapping
    public Mono<ResponseEntity<ProductPriceResponseDto>> getPricesByFilter(
        @Parameter(description = "Product identifier", example = "35455")
        @RequestParam Integer productId,
        @Parameter(description = "Brand identifier", example = "1")
        @RequestParam Integer brandId,
        @Parameter(description = "Date to apply the product price", example = "2020-07-31T13:31:21.45")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam LocalDateTime applicationDate
    ) throws ProductPriceNotFoundException {

        log.info(
            "Receive call to GET /api/v1/prices with params productId={}, brandId={} and applicationDate={}",
            productId, brandId, applicationDate
        );

        return findProductPriceUseCase.find(productId, brandId, applicationDate)
            .map(productPriceDtoMapper::toResponseDto)
            .map(ResponseEntity::ok)
            .doOnSuccess(productPrice -> log.info("Product price retrieved successfully"))
            .doOnError(e -> log.error("Error finding product price", e));
    }
}

